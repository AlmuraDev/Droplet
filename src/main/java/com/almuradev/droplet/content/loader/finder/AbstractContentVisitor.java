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
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.nio.file.Path;

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
  }

  @Override
  public void visitChild(final ChildContentLoader<C> loader, final C type, final Path path) {
    this.childLoader = loader;
    this.child = type;
    this.childPath = path;
  }

  @Override
  public void visitEntry(final Path path, final Provider<ContentBuilder> builder) {
    final FoundContentEntry<R, C> entry = this.createEntry(path, builder);
    this.childLoader.foundContent().offer(entry);
    this.entries.put(entry.childType(), entry);
  }

  protected abstract FoundContentEntry<R, C> createEntry(final Path path, final Provider<ContentBuilder> builder);

  @Override
  public FoundContent<R, C> foundContent() {
    return new FoundContent<>(this.entries);
  }
}
