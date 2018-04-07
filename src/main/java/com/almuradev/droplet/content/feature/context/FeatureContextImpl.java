/*
 * This file is part of droplet, licensed under the MIT License.
 *
 * Copyright (c) 2017-2018 AlmuraDev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.almuradev.droplet.content.feature.context;

import com.almuradev.droplet.content.feature.Feature;
import com.almuradev.droplet.content.feature.ProxiedFeature;
import com.almuradev.droplet.proxy.MethodHandleInvocationHandler;
import com.almuradev.droplet.proxy.Proxied;
import com.google.common.collect.MoreCollectors;
import net.kyori.lunar.Optionals;
import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FeatureContextImpl implements FeatureContext {
  private final Map<String, Entry<?>> entriesById = new HashMap<>();
  private final List<FeatureContextImpl> parents = new ArrayList<>();

  @Override
  public <F> F get(final Class<F> type, final Node node) throws XMLException {
    final Node id = node.attribute(Feature.ID_ATTRIBUTE_NAME).orElseThrow(() -> new XMLException("Could not find '" + Feature.ID_ATTRIBUTE_NAME + "' attribute"));
    return this.feature(type, id.value()).reference(node).get();
  }

  @Override
  public <F> F get(final Class<F> type, final String id) {
    return this.feature(type, id).get();
  }

  @Override
  public <F> F add(final Class<F> type, final Node node, final F feature) {
    return this.add(type, node.attribute(Feature.ID_ATTRIBUTE_NAME).map(Node::value).orElse(null), feature);
  }

  @Override
  public <F> F add(final Class<F> type, @Nullable final String id, final F feature) {
    // Don't insert a proxied feature.
    if(feature instanceof Proxied) {
      return feature;
    }

    // This feature has an id, and can be referenced.
    if(id != null) {
      /* @Nullable */ final Entry<F> entry = this.feature(type, id);
      if(!entry.virtual() && entry.feature != feature) {
        throw new IllegalStateException(type.getName() + " with id " + id + " already defined as " + entry.feature + ", cannot redefine as " + feature);
      }

      entry.feature = feature;
    }

    return feature;
  }

  private <F> Entry<F> feature(final Class<F> type, final String id) {
    return (Entry<F>) Optionals.first(
      Optional.ofNullable(this.entriesById.get(id)),
      this.parents.stream().map(parent -> parent.entriesById.get(id)).collect(MoreCollectors.toOptional())
    ).orElseGet(() -> this.entriesById.computeIfAbsent(id, key -> new Entry<>(type, id)));
  }

  @Override
  public List<XMLException> validate() {
    final List<XMLException> exceptions = new ArrayList<>();
    for(final Entry<?> entry : this.entriesById.values()) {
      if(entry.virtual()) {
        entry.references.forEach(reference -> exceptions.add(new XMLException(reference, "reference for " + entry.type.getName() + " with id " + entry.id + " has not been defined")));
      }
    }
    return exceptions;
  }

  @Override
  public void addParent(final FeatureContext that) {
    this.parents.add((FeatureContextImpl) that);
  }

  @Override
  public String toString() {
    return this.entriesById.toString();
  }

  private static class Entry<F> {
    final Class<F> type;
    @Nullable final String id;
    @Nullable F feature;
    final List<Node> references = new ArrayList<>();
    @Nullable F proxiedFeature;

    Entry(final Class<F> type, @Nullable final String id) {
      this.type = type;
      this.id = id;
    }

    boolean virtual() {
      return this.feature == null;
    }

    Entry<F> reference(final Node node) {
      this.references.add(node);
      return this;
    }

    F get() {
      if(this.feature != null) {
        return this.feature;
      }
      return this.proxy();
    }

    private F feature() {
      if(this.feature == null) {
        throw new IllegalStateException("feature of type " + this.type + " has not been provided for " + this.id);
      }
      return this.feature;
    }

    private F proxy() {
      if(this.proxiedFeature == null) {
        class ProxiedFeatureImpl extends MethodHandleInvocationHandler {
          @Nullable
          @Override
          protected Object object(final Method method) {
            return Entry.this.feature();
          }
        }
        this.proxiedFeature = (F) Proxy.newProxyInstance(this.type.getClassLoader(), this.proxyClasses(), new ProxiedFeatureImpl());
      }
      return this.proxiedFeature;
    }

    private Class<?>[] proxyClasses() {
      final List<Class<?>> classes = new ArrayList<>();

      classes.add(this.type);

      if(Feature.class.isAssignableFrom(this.type)) {
        classes.add(ProxiedFeature.class);
      } else {
        classes.add(Proxied.class);
      }

      return classes.toArray(new Class<?>[classes.size()]);
    }
  }
}
