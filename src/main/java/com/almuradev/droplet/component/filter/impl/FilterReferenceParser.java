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
import com.almuradev.droplet.component.filter.FilterTypeParser;
import com.almuradev.droplet.content.feature.context.FeatureContext;
import com.almuradev.droplet.content.inject.DynamicProvider;
import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class FilterReferenceParser implements FilterTypeParser<Filter> {
  public static final String ID = "filter";
  private final DynamicProvider<FeatureContext> featureContext;

  @Inject
  private FilterReferenceParser(final DynamicProvider<FeatureContext> featureContext) {
    this.featureContext = featureContext;
  }

  @Override
  public Filter throwingParse(final Node node) throws XMLException {
    return this.featureContext.get().get(Filter.class, node);
  }
}
