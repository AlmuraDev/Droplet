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
package com.almuradev.droplet.content.inject;

import com.almuradev.droplet.content.loader.RootContentLoader;
import com.almuradev.droplet.content.processor.Processor;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.google.common.reflect.TypeToken;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import net.kyori.violet.FriendlyTypeLiteral;
import net.kyori.violet.TypeArgument;
import net.kyori.violet.VBinder;

public interface RootBinder<C extends ContentType.Child, B extends ContentBuilder<?>> extends ContentBinder<B>, VBinder {
  TypeToken<B> builderType();

  /**
   * Bind the root type.
   *
   * @param type the root type
   */
  default void bindRootType(final ContentType.Root<C> type) {
    this.bind(new FriendlyTypeLiteral<ContentType.Root<C>>() {}.where(new TypeArgument<C>(type.childType()) {})).toInstance(type);
    this.inSet(ContentType.Root.class).addBinding().toInstance(type);
  }

  /**
   * Bind the root loader.
   *
   * @param implementation the loader
   */
  default void bindRootLoader(final TypeLiteral<? extends RootContentLoader<C>> implementation) {
    this.inSet(RootContentLoader.class).addBinding().toProvider(this.getProvider(Key.get(implementation)));
  }

  @Override
  default <P extends Processor<? extends B>> void bindProcessor(final Class<P> processorClass) {
    this.inSet(Key.get(new FriendlyTypeLiteral<Processor<? extends B>>() {}.where(new TypeArgument<B>(this.builderType()) {}), ForRoot.class)).addBinding().to(processorClass);
  }

  void installChild(final ChildModule<C>... modules);
}
