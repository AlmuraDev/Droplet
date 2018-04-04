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
package com.almuradev.droplet.component.filter.impl;

import com.almuradev.droplet.component.filter.Filter;
import com.almuradev.droplet.component.filter.FilterQuery;
import com.almuradev.droplet.component.filter.FilterResponse;

import java.util.List;

public abstract class AbstractMultipleFilter implements Filter {
  protected final List<Filter> filters;

  protected AbstractMultipleFilter(final List<Filter> filters) {
    this.filters = filters;
  }

  protected FilterResponse query(final FilterQuery query, final FilterResponse wanted, final FilterResponse unmatchedTarget) {
    FilterResponse unmatchedResponse = FilterResponse.ABSTAIN;
    for(Filter filter : this.filters) {
      final FilterResponse response = filter.query(query);
      if(response == wanted) {
        return response;
      } else if(response == unmatchedTarget) {
        unmatchedResponse = response;
      }
    }
    return unmatchedResponse;
  }
}
