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
import com.almuradev.droplet.util.IndentingLogger;
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

public class RootContentLoaderImpl<C extends ContentType.Child, B extends ContentBuilder<?>> implements RootContentLoader<C> {
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
    final IndentingLogger logger = new IndentingLogger(this.logger, 1);
    logger.debug("Parsing {} content...", this.type.id());
    logger.push();

    // Process type-level includes first
    this.foundContent.typeIncludes().ifPresent(typeIncludes -> {
      logger.debug("Parsing includes...");
      logger.push(() -> this.processIncludes(logger, typeIncludes));
    });

    logger.debug("Parsing children...");
    logger.push();
    this.children.stream()
      .map(ChildContentLoader::type)
      .filter(child -> !this.foundContent.entries(child).isEmpty())
      .forEach(child -> {
        logger.debug("Parsing {} children...", child.id());

        try(final IndentingLogger $ = logger.push()) {
          final List<FoundEntry> includes = this.foundContent.childIncludes().map(childIncludes -> childIncludes.get(child)).orElse(Collections.emptyList());

          // Followed by children-level includes
          if(!includes.isEmpty()) {
            logger.debug("Parsing includes...");
            logger.push(() -> this.processIncludes(logger, includes));
          }

          // and then the actual content
          logger.debug("Parsing entries...");
          try(final IndentingLogger $$ = logger.push()) {
            this.foundContent.entries(child).forEach(entry -> {
              logger.debug("Parsing {}", entry.toString());
              this.featureContext.set(entry.context());
              this.inheritContext(entry);
              final Element rootElement = entry.rootElement();
              if(!this.validateSpec(logger, entry.spec())) {
                return;
              }
              final List<Element> childrenElements = rootElement.getChildren(entry.rootType().rootElement());
              final Node rootNode = Node.of(rootElement);
              this.globalProcessors.forEach(Exceptions.rethrowConsumer(processor -> processor.process(rootNode)));
              if(!childrenElements.isEmpty()) {
                childrenElements.forEach(child2 -> {
                  final Node node = Node.of(child2);
                  this.processors.forEach(Exceptions.rethrowConsumer(processor -> ((Processor) processor).process(node, entry.builder())));
                  this.childLoader(entry.childType()).processors().forEach(Exceptions.rethrowConsumer(processor -> processor.process(node, entry.builder())));
                });
              } else {
                logger.push(() -> logger.debug("No children with name '" + entry.rootType().rootElement() + "' - invalidating"));
                entry.invalidate();
              }
            });
          }
        }
      });
    this.featureContext.set(null);
  }

  private void processIncludes(final IndentingLogger logger, final List<FoundEntry> typeIncludes) {
    typeIncludes.forEach(entry -> {
      logger.debug("Parsing {}", entry.toString());
      this.featureContext.set(entry.context());
      final Element rootElement = entry.rootElement();
      if(!this.validateSpec(logger, entry.spec())) {
        return;
      }
      final Node rootNode = Node.of(rootElement);
      this.globalProcessors.forEach(Exceptions.rethrowConsumer(processor -> processor.process(rootNode)));
    });
    this.featureContext.set(null);
  }

  private boolean validateSpec(final IndentingLogger logger, final ContentSpec spec) {
    if(!spec.atLeast(ContentSpec.CURRENT)) {
      logger.push(() -> logger.error("Specification version {} is below minimum supported version {}", spec, ContentSpec.CURRENT));
      return false;
    }
    return true;
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
    final IndentingLogger logger = new IndentingLogger(this.logger, 1);
    logger.debug("Validating {} content...", this.type.id());
    logger.push();
    this.children.stream()
      .map(ChildContentLoader::type)
      .filter(child -> !this.foundContent.entries(child).isEmpty())
      .forEach(child -> {
        logger.debug("Validating {} content...", child.id());
        try(final IndentingLogger $ = logger.push()) {
          this.foundContent.entries(child).forEach(entry -> {
            entry.context().validate().forEach(exception -> {
              final StringBuilder sb = new StringBuilder();
              sb.append(entry.absolutePath().toString());
              /* @Nullable */ final Node node = exception.node();
              if(node != null) {
                Nodes.appendLocation(node, sb);
              }
              sb.append(": ").append(exception.getMessage());
              logger.error("{}", sb.toString());
            });
          });
        }
      });
  }

  @Override
  public final void queue() {
    final IndentingLogger logger = new IndentingLogger(this.logger, 1);
    logger.debug("Queuing {} content...", this.type.id());
  }

  public final FoundContent<ContentType.Root<C>, C> foundContent() {
    return this.foundContent;
  }

  @Override
  public final String toString() {
    return this.getClass().getName() + '{' + this.type.toString() + '}';
  }
}
