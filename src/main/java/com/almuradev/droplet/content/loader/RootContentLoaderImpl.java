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
package com.almuradev.droplet.content.loader;

import com.almuradev.droplet.content.feature.context.FeatureContext;
import com.almuradev.droplet.content.inject.DynamicProvider;
import com.almuradev.droplet.content.inject.ForRoot;
import com.almuradev.droplet.content.loader.finder.ContentFinder;
import com.almuradev.droplet.content.loader.finder.FoundContent;
import com.almuradev.droplet.content.loader.finder.FoundContentEntry;
import com.almuradev.droplet.content.loader.finder.FoundEntry;
import com.almuradev.droplet.content.processor.GlobalProcessor;
import com.almuradev.droplet.content.processor.Processor;
import com.almuradev.droplet.content.spec.ContentSpec;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.almuradev.droplet.parser.Nodes;
import com.almuradev.droplet.util.Logging;
import com.google.common.collect.MoreCollectors;
import net.kyori.lunar.exception.Exceptions;
import net.kyori.xml.node.Node;
import org.jdom2.Element;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public abstract class RootContentLoaderImpl<C extends ContentType.Child, B extends ContentBuilder<?>> implements RootContentLoader<C> {
  private final Map<C, ChildContentLoader<C>> childLoaderByChild = new HashMap<>();
  @Inject private Logger logger;
  @Inject private ContentType.Root<C> type;
  @Inject private ContentFinder finder;
  @Inject private Set<ChildContentLoader<C>> children;
  @Inject private Set<GlobalProcessor> globalProcessors;
  @ForRoot @Inject private Set<Processor<? extends B>> processors;
  @Inject private DynamicProvider<FeatureContext> featureContext;
  private FoundContent<ContentType.Root<C>, C> foundContent;

  @Override
  public final void discover() {
    this.foundContent = this.finder.find(this.type, this.children);
  }

  @Override
  public final void parse() {
    this.logger.debug("{}Parsing {} content...", Logging.indent(1), this.type.id());

    this.foundContent.typeIncludes().ifPresent(typeIncludes -> {
      this.logger.debug("{}Parsing root includes...", Logging.indent(2));
      this.processIncludes(Collections.emptyList(), typeIncludes, 2);
    });

    final long entries = this.children.stream()
      .map(ChildContentLoader::type)
      .filter(child -> !this.foundContent.entries(child).isEmpty())
      .mapToLong(child -> {
        this.logger.debug("{}Parsing {} content...", Logging.indent(2), child.id());
        final List<FoundEntry> includes = this.foundContent.childIncludes().map(childIncludes -> childIncludes.get(child)).orElse(Collections.emptyList());
        if(!includes.isEmpty()) {
          this.logger.debug("{}Parsing includes...", Logging.indent(3));
          this.processIncludes(this.foundContent.typeIncludes().orElse(Collections.emptyList()), includes, 3);
        }
        this.logger.debug("{}Parsing entries...", Logging.indent(3));
        return this.foundContent.entries(child).stream().peek(entry -> {
          this.logger.debug("{}Parsing {}", Logging.indent(4), entry.toString());
          this.featureContext.set(entry.context());
          this.inheritContext(entry);
          final Element rootElement = entry.rootElement();
          if(!entry.spec().atLeast(ContentSpec.CURRENT)) {
            this.logger.error("{}Specification version {} is below minimum supported version {}", Logging.indent(5), entry.spec(), ContentSpec.CURRENT);
            return;
          }
          final Node rootNode = Node.of(rootElement);
          this.globalProcessors.forEach(Exceptions.rethrowConsumer(processor -> processor.process(rootNode)));
          rootElement.getChildren(entry.rootType().rootElement()).forEach(child2 -> {
            final Node node = Node.of(child2);
            this.processors.forEach(Exceptions.rethrowConsumer(processor -> ((Processor) processor).process(node, entry.builder())));
            this.childLoader(entry.childType()).processors().forEach(Exceptions.rethrowConsumer(processor -> processor.process(node, entry.builder())));
          });
        }).count();
      }).sum();
    this.featureContext.set(null);
    this.logger.debug("{}{} {} parsed", Logging.indent(2), entries, entries == 1 ? "entry" : "entries");
  }

  private void processIncludes(final List<FoundEntry> inherit, final List<FoundEntry> typeIncludes, final int indentStart) {
    typeIncludes.forEach(entry -> {
      this.logger.debug("{}Parsing {}", Logging.indent(indentStart + 1), entry.toString());
      this.featureContext.set(entry.context());
      final Element rootElement = entry.rootElement();
      if(!entry.spec().atLeast(ContentSpec.CURRENT)) {
        this.logger.error("{}Specification version {} is below minimum supported version {}", Logging.indent(indentStart + 2), entry.spec(), ContentSpec.CURRENT);
        return;
      }
      final Node rootNode = Node.of(rootElement);
      this.globalProcessors.forEach(Exceptions.rethrowConsumer(processor -> processor.process(rootNode)));
    });
    this.featureContext.set(null);
  }

  private void inheritContext(final FoundContentEntry<ContentType.Root<C>, C> entry) {
    final FeatureContext context = entry.context();
    this.foundContent.typeIncludes().ifPresent(typeIncludes -> typeIncludes.forEach(include -> context.addParent(include.context())));
    this.foundContent.childIncludes().ifPresent(childIncludes -> childIncludes.get(entry.childType()).forEach(include -> context.addParent(include.context())));
  }

  private ChildContentLoader<C> childLoader(final C child) {
    return this.childLoaderByChild.computeIfAbsent(child, key -> this.children.stream().filter(c -> c.type() == child).collect(MoreCollectors.onlyElement()));
  }

  @Override
  public final void validate() {
    this.logger.debug("{}Validating {} content...", Logging.indent(1), this.type.id());
    this.children.stream()
      .map(ChildContentLoader::type)
      .filter(child -> !this.foundContent.entries(child).isEmpty())
      .forEach(child -> {
        this.logger.debug("{}Validating {} content...", Logging.indent(2), child.id());
        this.foundContent.entries(child).forEach(entry -> {
          entry.context().validate().forEach(exception -> {
            final StringBuilder sb = new StringBuilder();
            sb.append(entry.absolutePath().toString());
            /* @Nullable */ final Node node = exception.node();
            if(node != null) {
              Nodes.appendLocation(node, sb);
            }
            sb.append(": ").append(exception.getMessage());
            this.logger.error("{}{}", Logging.indent(3), sb.toString());
          });
        });
      });
  }

  @Override
  public final void queue() {
    this.logger.debug("{}Queuing {} content...", Logging.indent(1), this.type.id());
  }

  public final FoundContent<ContentType.Root<C>, C> foundContent() {
    return this.foundContent;
  }

  @Override
  public final String toString() {
    return this.getClass().getName() + '{' + this.type.toString() + '}';
  }
}
