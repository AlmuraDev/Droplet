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
import com.almuradev.droplet.content.type.ContentType;
import com.google.common.reflect.TypeToken;
import com.google.inject.Key;
import com.google.inject.Module;
import net.kyori.violet.DuplexModule;

/**
 * A child content type module.
 *
 * @param <C> the child type
 */
public interface ChildModule<C extends ContentType.Child> extends ChildBinder<C>, Module {
  abstract class Impl<C extends ContentType.Child> extends DuplexModule implements ChildModule<C> {
    private final TypeToken<C> childType = new TypeToken<C>(this.getClass()) {};

    @Override
    public final TypeToken<C> childType() {
      return this.childType;
    }

    @Override
    protected final void configure() {
      this.inSet(Key.get(Processor.class, ForChild.class)); // init processor set
      this.configure0();
    }

    protected abstract void configure0();
  }
}
