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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FeatureContextImpl implements FeatureContext {
  private final Map<Class<?>, IdMap> byType = new HashMap<>();

  @Override
  public <F> Optional<F> find(final Class<F> type, final String id) {
    /* @Nullable */ final Object feature = this.idMap(type).byId.get(id);
    if(feature == null) {
      return Optional.empty();
    }
    return Optional.of(type.cast(feature));
  }

  @Override
  public <F> F add(final Class<F> type, final FeatureId id, final F feature) {
    this.idMap(type).addFeature(feature, id);
    return feature;
  }

  private IdMap idMap(final Class<?> type) {
    return this.byType.computeIfAbsent(type, key -> new IdMap());
  }

  private static class IdMap {
    private final Map<String, Object> byId = new HashMap<>();
    private final Map<String, Id> nextId = new HashMap<>();

    <F> void addFeature(final F feature, final FeatureId id) {
      if(feature instanceof Feature && !((Feature) feature).canBeReferenced()) {
        return;
      }

      /* @Nullable */ final Object oldFeature = this.byId.get(id.get());
      final String stringId;
      if(oldFeature != null) {
        if(!id.auto()) {
          throw new IllegalStateException("conflict in feature context: " + id.get() + " already defined as " + oldFeature + ", cannot redefine as " + feature);
        }
        stringId = this.nextId.computeIfAbsent(id.get(), Id::new).next();
      } else {
        stringId = !id.auto() ? id.get() : this.nextId.computeIfAbsent(id.get(), Id::new).next();
      }
      this.byId.put(stringId, feature);
    }

    @Override
    public String toString() {
      return this.byId.toString();
    }
  }

  @Override
  public String toString() {
    return this.byType.toString();
  }

  private static class Id {
    private final String id;
    private int value = 1;

    private Id(String id) {
      this.id = id;
    }

    public String next() {
      return this.id + "-" + this.value++;
    }
  }
}
