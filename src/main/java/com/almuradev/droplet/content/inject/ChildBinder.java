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

import com.almuradev.droplet.content.loader.ChildContentLoader;
import com.almuradev.droplet.content.processor.Processor;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.google.common.reflect.TypeToken;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import net.kyori.violet.EvenMoreTypes;
import net.kyori.violet.FriendlyTypeLiteral;
import net.kyori.violet.TypeArgument;
import net.kyori.violet.VBinder;
import net.kyori.violet.VDuplexBinder;

public interface ChildBinder<C extends ContentType.Child> extends ContentBinder<ContentBuilder<?>>, VDuplexBinder {
  TypeToken<C> childType();

  default void bindChildType(final C type) {
    this.bind(EvenMoreTypes.literal(this.childType())).toInstance(type);
    this.inSet(new FriendlyTypeLiteral<C>() {}.where(new TypeArgument<C>(this.childType()) {})).addBinding().toInstance(type);
  }

  default void bindChildLoader(final TypeLiteral<? extends ChildContentLoader<C>> implementation) {
    VBinder.of(this.publicBinder()).inSet(Key.get(new FriendlyTypeLiteral<ChildContentLoader<C>>() {}.where(new TypeArgument<C>(this.childType()) {}))).addBinding().toProvider(this.getProvider(Key.get(implementation)));
  }

  default <I extends ContentBuilder> LinkedBindingBuilder<I> bindBuilder(final Class<I> interfaceClass) {
    this.bind(ContentBuilder.class).to(interfaceClass);
    return this.bind(interfaceClass);
  }

  @Override
  default <P extends Processor<? extends ContentBuilder<?>>> void bindProcessor(final Class<P> processorClass) {
    this.inSet(Key.get(Processor.class, ForChild.class)).addBinding().to(processorClass);
  }
}
