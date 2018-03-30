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

import java.util.Objects;
import java.util.Random;

public final class IntRangeImpl implements IntRange {
  /**
   * The minimum value.
   */
  private final int min;
  /**
   * The maximum value.
   */
  private final int max;

  public IntRangeImpl(final int value) {
    this(value, value);
  }

  public IntRangeImpl(final int min, final int max) {
    if(min > max) {
      throw new IllegalStateException(String.format("min (%s) is greater than max (%s)", min, max));
    }

    this.min = min;
    this.max = max;
  }

  @Override
  public int min() {
    return this.min;
  }

  @Override
  public int max() {
    return this.max;
  }

  @Override
  public int random(final Random random) {
    return random.nextInt((this.max - this.min) + 1) + this.min;
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) {
      return true;
    }
    if(!(other instanceof IntRange)) {
      return false;
    }
    final IntRange that = (IntRange) other;
    return Integer.compare(this.min, that.min()) == 0
      && Integer.compare(this.max, that.max()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.min, this.max);
  }
}
