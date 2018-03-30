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

public class DefaultedFilteredMinimumVariancedIntListImpl implements DefaultedFilteredMinimumVariancedIntList {
  private final List<FilterLinked<MinimumVariancedInt>> filtered;
  private final MinimumVariancedInt defaultValue;

  public DefaultedFilteredMinimumVariancedIntListImpl(final List<FilterLinked<MinimumVariancedInt>> filtered, final MinimumVariancedInt defaultValue) {
    this.filtered = filtered;
    this.defaultValue = defaultValue;
  }

  @Nullable
  @Override
  public MinimumVariancedInt oneOrDefault(final FilterQuery query) {
    for(final FilterLinked<MinimumVariancedInt> entry : this.filtered) {
      if(entry.test(query)) {
        return entry.value();
      }
    }
    return this.defaultValue;
  }

  static class Builder implements DefaultedFilteredMinimumVariancedIntList.Builder {
    private final List<FilterLinked<MinimumVariancedInt>> filtered = new ArrayList<>();
    private MinimumVariancedInt defaultValue;

    @Override
    public void filtered(final FilterLinked<MinimumVariancedInt> filtered) {
      this.filtered.add(filtered);
    }

    @Override
    public void defaultValue(final MinimumVariancedInt defaultValue) {
      this.defaultValue = defaultValue;
    }

    @Override
    public DefaultedFilteredMinimumVariancedIntList build() {
      return new DefaultedFilteredMinimumVariancedIntListImpl(this.filtered, this.defaultValue);
    }
  }
}
