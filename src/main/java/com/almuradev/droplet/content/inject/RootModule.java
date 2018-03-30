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

import com.almuradev.droplet.content.processor.Processor;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.google.common.reflect.TypeToken;
import com.google.inject.Key;
import com.google.inject.Module;
import net.kyori.violet.AbstractModule;
import net.kyori.violet.DuplexBinder;
import net.kyori.violet.FriendlyTypeLiteral;
import net.kyori.violet.TypeArgument;

/**
 * A root content type module.
 *
 * @param <C> the child type
 * @param <B> the builder type
 */
public interface RootModule<C extends ContentType.Child, B extends ContentBuilder<?>> extends Module, RootBinder<C, B> {
  abstract class Impl<C extends ContentType.Child, B extends ContentBuilder<?>> extends AbstractModule implements RootModule<C, B> {
    private final TypeToken<B> builderType = new TypeToken<B>(this.getClass()) {};

    @Override
    public final TypeToken<B> builderType() {
      return this.builderType;
    }

    @Override
    @SafeVarargs
    public final void installChild(final ChildModule<C>... modules) {
      final DuplexBinder binder = DuplexBinder.create(this.binder());
      for(final ChildModule<C> module : modules) {
        binder.install(module);
      }
    }

    @Override
    protected final void configure() {
      this.inSet(Key.get(new FriendlyTypeLiteral<Processor<? extends B>>() {}.where(new TypeArgument<B>(this.builderType) {}), ForRoot.class)); // init processor set
      this.configure0();
    }

    protected abstract void configure0();
  }
}
