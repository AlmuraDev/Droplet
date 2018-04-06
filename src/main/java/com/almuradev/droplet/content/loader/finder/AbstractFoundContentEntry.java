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

import com.almuradev.droplet.content.feature.context.FeatureContext;
import com.almuradev.droplet.content.feature.context.FeatureContextImpl;
import com.almuradev.droplet.content.spec.ContentSpec;
import com.almuradev.droplet.content.type.Content;
import com.almuradev.droplet.content.type.ContentType;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class AbstractFoundContentEntry<R extends ContentType.Root<C>, C extends ContentType.Child> implements FoundContentEntry<R, C> {
  private final FeatureContext context = new FeatureContextImpl();
  private final R rootType;
  private final C childType;
  @Nullable private ContentSpec spec;
  private Content result;

  protected AbstractFoundContentEntry(final R rootType, final C childType) {
    this.rootType = rootType;
    this.childType = childType;
  }

  @Override
  public R rootType() {
    return this.rootType;
  }

  @Override
  public C childType() {
    return this.childType;
  }

  @Override
  public FeatureContext context() {
    return this.context;
  }

  @Override
  public ContentSpec spec() {
    if(this.spec == null) {
      this.spec = ContentSpec.parse(requireNonNull(this.rootElement().getAttribute("spec"), "missing spec attribute").getValue());
    }
    return this.spec;
  }

  @Override
  public Content result() {
    if(this.result == null) {
      this.result = this.builder().build();
    }
    return this.result;
  }

  @Override
  public String toString() {
    return this.key().toString() + " (" + this.absolutePath() + ')';
  }
}
