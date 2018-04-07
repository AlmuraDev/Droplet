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
package com.almuradev.droplet.content.loader.finder;

import com.almuradev.droplet.content.loader.ChildContentLoader;
import com.almuradev.droplet.content.loader.DocumentFactory;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.almuradev.droplet.util.PathVisitor;
import com.google.common.base.Suppliers;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.kyori.lunar.exception.Exceptions;
import org.jdom2.Element;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javax.inject.Provider;

public abstract class AbstractContentVisitor<R extends ContentType.Root<C>, C extends ContentType.Child> implements ContentVisitor<R, C> {
  protected String namespace;
  protected Path namespacePath;
  protected R type;
  protected Path typePath;
  protected ChildContentLoader<C> childLoader;
  protected C child;
  protected Path childPath;
  protected final ListMultimap<C, FoundContentEntry<R, C>> entries = ArrayListMultimap.create();
  private DocumentFactory documentFactory;
  protected List<FoundEntry> typeIncludes;
  private ListMultimap<C, FoundEntry> childIncludes;

  @Override
  public void visitRoot(final Path path) {
  }

  @Override
  public void visitNamespace(final Path path) {
    this.namespace = path.getFileName().toString();
    this.namespacePath = path;
  }

  @Override
  public void visitContent(final Path path) {
  }

  @Override
  public void visitType(final R type, final Path path) {
    this.type = type;
    this.typePath = path;

    final Path include = path.resolve(ContentFinder.INCLUDE_DIRECTORY_NAME);
    if(Files.isDirectory(include)) {
      if(this.typeIncludes == null) {
        this.typeIncludes = new ArrayList<>();
      }
      this.typeIncludes.addAll(this.includes(include));
    }
  }

  @Override
  public void visitChild(final ChildContentLoader<C> loader, final C type, final Path path) {
    this.childLoader = loader;
    this.child = type;
    this.childPath = path;

    final Path include = path.resolve(ContentFinder.INCLUDE_DIRECTORY_NAME);
    if(Files.isDirectory(include)) {
      if(this.childIncludes == null) {
        this.childIncludes = ArrayListMultimap.create();
      }
      this.childIncludes.putAll(type, this.includes(include));
    }
  }

  @Override
  public void visitEntry(final Path path, final Provider<ContentBuilder> builder) {
    final FoundContentEntry<R, C> entry = this.createEntry(path, builder);
    this.childLoader.foundContent().offer(entry);
    this.entries.put(entry.childType(), entry);
  }

  protected DocumentFactory documentFactory() {
    if(this.documentFactory == null) {
      this.documentFactory = new DocumentFactory(Arrays.asList(this.childPath, this.typePath, this.namespacePath));
    }
    return this.documentFactory;
  }

  protected abstract FoundContentEntry<R, C> createEntry(final Path path, final Provider<ContentBuilder> builder);

  private List<FoundEntry> includes(final Path path) {
    final List<FoundEntry> includes = new ArrayList<>();
    PathVisitor.walk(path, new PathVisitor() {
      @Override
      public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) {
        if(ContentFinder.XML_MATCHER.matches(file)) {
          includes.add(AbstractContentVisitor.this.include(file));
        }
        return FileVisitResult.CONTINUE;
      }
    });
    return includes;
  }

  private FoundEntry include(final Path path) {
    return new AbstractFoundEntry() {
      final Supplier<Element> rootElement = Suppliers.memoize(Exceptions.rethrowSupplier(() -> AbstractContentVisitor.this.documentFactory().read(this.absolutePath()).getRootElement())::get);

      @Override
      public Path absolutePath() {
        return path;
      }

      @Override
      public Element rootElement() {
        return this.rootElement.get();
      }

      @Override
      public boolean valid() {
        return true;
      }

      @Override
      public void invalidate() {
      }
    };
  }

  @Override
  public FoundContent<R, C> foundContent() {
    return new FoundContent<>(this.entries, this.typeIncludes, this.childIncludes);
  }
}
