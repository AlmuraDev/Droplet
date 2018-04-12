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
package com.almuradev.droplet.registry.reference;

import com.almuradev.droplet.registry.Registry;
import com.almuradev.droplet.registry.RegistryKey;
import com.google.common.base.MoreObjects;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class RegistryReferenceImpl<V> extends LazyRegistryReference<V> {
  private final Registry<V> registry;
  private final RegistryKey key;

  public RegistryReferenceImpl(final Registry<V> registry, final RegistryKey key) {
    this.registry = registry;
    this.key = key;
  }

  @Override
  protected @Nullable V load() {
    return this.registry.get(this.key);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .addValue(this.key)
      .toString();
  }
}
