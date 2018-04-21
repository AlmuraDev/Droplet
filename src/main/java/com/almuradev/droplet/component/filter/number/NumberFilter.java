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
package com.almuradev.droplet.component.filter.number;

import net.kyori.fragment.filter.FilterQuery;
import net.kyori.fragment.filter.TypedFilter;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkState;

/*
<all>
    <skills:level op="greater-than" value=5/>
    <skills:level op="lesser-than" value=10/>
</all>
syntax is wrong
but you get the idea
Yes? No?
kashike - Yesterday at 22:08
and for greater-than-or-equal-to?
Zidane - Yesterday at 22:08
Longwinded string or self as you did before
My qualm with self is no one will read it and say "Yeah that means inclusive to the value"
This is a spot where XML is fucking annoying
Using math symbols would make this very clean
I am going to say do longwinded string
Think that covers it?
Can't think of any edge-case
 */
public interface NumberFilter extends TypedFilter<NumberQuery> {
  NumberComparator COMPARATOR = new NumberComparator();

  @Override
  default boolean queryable(final FilterQuery query) {
    return query instanceof NumberQuery;
  }

  abstract class Impl implements NumberFilter {
    protected final Number number;

    public Impl(final Number number) {
      checkState(number instanceof Comparable, "number is not comparable");
      this.number = number;
    }

    protected int compare(final NumberQuery query) {
      return COMPARATOR.compare((Comparable) this.number, (Comparable) query.number());
    }
  }

  class NumberComparator implements Comparator<Comparable> {
    @Override
    public int compare(final Comparable o1, final Comparable o2) {
      return o1.compareTo(o2);
    }
  }
}
