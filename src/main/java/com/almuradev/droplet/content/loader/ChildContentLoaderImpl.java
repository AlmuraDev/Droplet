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

import com.almuradev.droplet.content.inject.ForChild;
import com.almuradev.droplet.content.loader.finder.FoundContent;
import com.almuradev.droplet.content.loader.finder.FoundContentImpl;
import com.almuradev.droplet.content.processor.Processor;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

public class ChildContentLoaderImpl<C extends ContentType.Child> implements ChildContentLoader<C> {
  @Inject private C type;
  @Inject private Provider<ContentBuilder> builder;
  @ForChild @Inject private Set<Processor> processors;
  private final FoundContentImpl<?, C> foundContent = new FoundContentImpl<>();

  @Override
  public C type() {
    return this.type;
  }

  @Override
  public Provider<ContentBuilder> builder() {
    return this.builder;
  }

  @Override
  public Set<Processor> processors() {
    return this.processors;
  }

  @Override
  public FoundContent<?, C> foundContent() {
    return this.foundContent;
  }

  @Override
  public String toString() {
    return this.getClass().getName() + '{' + this.type.toString() + '}';
  }
}
