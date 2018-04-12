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
package com.almuradev.droplet.registry;

import com.almuradev.droplet.registry.reference.ComputableRegistryReference;
import com.almuradev.droplet.registry.reference.RegistryReference;
import com.almuradev.droplet.registry.reference.RegistryReferenceImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Optional;

/**
 * A registry of {@code K}s to {@code V}.
 *
 * @param <V> the type stored in this registry
 */
public interface Registry<V> {
  /**
   * Gets a collection of all values.
   *
   * @return a collection of all values
   */
  @NonNull Collection<V> all();

  /**
   * Gets a value from this registry.
   *
   * @param key the key
   * @return the value
   */
  @Nullable V get(final @NonNull RegistryKey key);

  /**
   * Puts a value in this registry.
   *
   * @param key the key
   * @param value the value
   */
  void put(final @NonNull RegistryKey key, final @NonNull V value);

  /**
   * Finds a value in this registry.
   *
   * @param key the key
   * @return the value
   */
  default @NonNull Optional<V> find(final @NonNull RegistryKey key) {
    return Optional.ofNullable(this.get(key));
  }

  /**
   * Creates a reference to a value in this registry.
   *
   * @param key the key
   * @return the value reference
   */
  default @NonNull RegistryReference<V> ref(final @NonNull RegistryKey key) {
    return new RegistryReferenceImpl<>(this, key);
  }

  /**
   * Creates a reference to a value in this registry that can be computed on request.
   *
   * @param computable the computable
   * @return the value reference
   */
  default @NonNull RegistryReference<V> ref(final @NonNull RegistryComputable<V> computable) {
    return new ComputableRegistryReference<>(this, computable);
  }
}
