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
package com.almuradev.droplet.component.range;

import com.almuradev.droplet.component.filter.FilterLinked;
import com.almuradev.droplet.component.filter.FilterQuery;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;

public interface IntRange {
  /**
   * Gets the minimum value.
   *
   * <p>This may be the same as {@link #max()}.</p>
   *
   * @return the minimum value
   */
  int min();

  /**
   * Gets the maximum value.
   *
   * <p>This may be the same as {@link #min()}.</p>
   *
   * @return the maximum value
   */
  int max();

  /**
   * Tests if this range contains {@code value}.
   *
   * @param value the value
   * @return {@code true} if this range contains the value, {@code false} otherwise
   */
  default boolean contains(final int value) {
    return value >= this.min() && value <= this.max();
  }

  /**
   * Gets a random value within this range.
   *
   * @param random the random
   * @return the value
   */
  int random(final Random random);

  interface Filtered extends IntRange, com.almuradev.droplet.component.filter.Filtered {
    @Nullable
    static <T> IntRange cachingSearch(final List<FilterLinked<IntRange>> list, final Map<T, IntRange> map, final T biome, final FilterQuery query, final IntRange defaultValue) {
      @Nullable IntRange found = map.get(biome);
      if(found == null) {
        for(final FilterLinked<IntRange> entry : list) {
          if(entry.test(query)) {
            final IntRange range = entry.value();
            map.put(biome, range);
            found = range;
            break;
          }
        }
      }

      if(found == null && defaultValue != null) {
        map.put(biome, defaultValue);
        found = defaultValue;
      }

      return found;
    }
  }
}
