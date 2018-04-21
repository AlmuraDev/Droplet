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
package com.almuradev.droplet.component.filter.number.impl;

import com.almuradev.droplet.component.filter.number.NumberFilter;
import com.almuradev.droplet.component.filter.number.NumberFilterParser;
import com.almuradev.droplet.component.filter.number.NumberQuery;
import net.kyori.fragment.filter.FilterResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LessThanFilter extends NumberFilter.Impl {
  public LessThanFilter(final Number number) {
    super(number);
  }

  @Override
  public FilterResponse typedQuery(final @NonNull NumberQuery query) {
    return FilterResponse.from(this.compare(query) == 1);
  }

  public static class Parser extends NumberFilterParser<LessThanFilter> {
    public Parser() {
      super(LessThanFilter::new);
    }
  }
}
