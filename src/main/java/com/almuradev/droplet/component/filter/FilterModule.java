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
package com.almuradev.droplet.component.filter;

import com.almuradev.droplet.component.filter.impl.AllFilterParser;
import com.almuradev.droplet.component.filter.impl.AnyFilterParser;
import com.almuradev.droplet.component.filter.impl.FilterReferenceParser;
import com.almuradev.droplet.component.filter.impl.NotFilterParser;
import com.almuradev.droplet.parser.ParserBinder;
import net.kyori.fragment.filter.Filter;
import net.kyori.violet.AbstractModule;

public final class FilterModule extends AbstractModule implements FilterBinder, ParserBinder {
  @Override
  protected void configure() {
    this.bindParser(Filter.class).to(RootFilterParserImpl.class);
    this.bind(FilterParser.class).to(RootFilterParserImpl.class);

    this.bindFilter("all").to(AllFilterParser.class);
    this.bindFilter("any").to(AnyFilterParser.class);
    this.bindFilter(FilterReferenceParser.ID).to(FilterReferenceParser.class);
    this.bindFilter("not").to(NotFilterParser.class);
  }
}
