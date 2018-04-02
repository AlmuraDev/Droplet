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

import java.nio.file.Path;

import javax.inject.Provider;

public abstract class AbstractContentVisitor<R extends ContentType.Root<C>, C extends ContentType.Child> implements ContentVisitor<R, C> {
  private final FoundContentBuilder<R, C> builder;

  protected AbstractContentVisitor(final FoundContentBuilder<R, C> builder) {
    this.builder = builder;
  }

  @Override
  public void visitCore(final Path path) {
  }

  @Override
  public void visitNamespace(final Path path) {
    this.builder.namespace(path.getFileName().toString(), path);
  }

  @Override
  public void visitContent(final Path path) {
  }

  @Override
  public void visitRoot(final R type, final Path path) {
    this.builder.root(type, path);
  }

  @Override
  public void visitChild(final ChildContentLoader<C> loader, final C type, final Path path) {
    this.builder.child(loader, type, path);
  }

  @Override
  public void visitEntry(final Path path, final Provider<ContentBuilder> builder) {
    this.builder.entry(path, builder);
  }

  @Override
  public FoundContent<R, C> foundContent() {
    return this.builder.build();
  }
}
