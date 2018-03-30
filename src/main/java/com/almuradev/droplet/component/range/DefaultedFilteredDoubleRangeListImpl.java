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

import java.util.ArrayList;
import java.util.List;

public final class DefaultedFilteredDoubleRangeListImpl implements DefaultedFilteredDoubleRangeList {
  private final List<FilterLinked<DoubleRange>> filtered;
  private final DoubleRange defaultValue;

  public DefaultedFilteredDoubleRangeListImpl(final List<FilterLinked<DoubleRange>> filtered, final DoubleRange defaultValue) {
    this.filtered = filtered;
    this.defaultValue = defaultValue;
  }

  @Nullable
  @Override
  public DoubleRange oneOrDefault(final FilterQuery query) {
    for(final FilterLinked<DoubleRange> entry : this.filtered) {
      if(entry.test(query)) {
        return entry.value();
      }
    }
    return this.defaultValue;
  }

  static class Builder implements DefaultedFilteredDoubleRangeList.Builder {
    private final List<FilterLinked<DoubleRange>> filtered = new ArrayList<>();
    private DoubleRange defaultValue;

    @Override
    public void filtered(final FilterLinked<DoubleRange> filtered) {
      this.filtered.add(filtered);
    }

    @Override
    public void defaultValue(final DoubleRange defaultValue) {
      this.defaultValue = defaultValue;
    }

    @Override
    public DefaultedFilteredDoubleRangeList build() {
      return new DefaultedFilteredDoubleRangeListImpl(this.filtered, this.defaultValue);
    }
  }
}
