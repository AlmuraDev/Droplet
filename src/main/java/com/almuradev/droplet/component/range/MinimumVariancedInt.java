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

import net.kyori.fragment.filter.Filter;
import net.kyori.fragment.filter.FilterQuery;
import net.kyori.lunar.collection.MoreIterables;

import java.util.List;
import java.util.Random;

public class MinimumVariancedInt {
  private final int min;
  private final IntRange variance;

  public MinimumVariancedInt(final int min, final IntRange variance) {
    this.min = min;
    this.variance = variance;
  }

  public int get(final Random random) {
    return this.min + this.variance.random(random);
  }

  public static class Filtered extends MinimumVariancedInt implements com.almuradev.droplet.component.filter.Filtered {
    private final Filter filter;

    public Filtered(final Filter filter, final int min, final IntRange variance) {
      super(min, variance);
      this.filter = filter;
    }

    @Override
    public Filter filter() {
      return this.filter;
    }

    public static int matchQueryOrRandom(final List<Filtered> filtereds, final FilterQuery query, final Random random) {
      for(final Filtered filtered : filtereds) {
        if(filtered.filter().allowed(query)) {
          return filtered.get(random);
        }
      }
      return MoreIterables.random(filtereds).get(random);
    }
  }
}
