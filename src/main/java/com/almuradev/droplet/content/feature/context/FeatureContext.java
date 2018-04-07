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
package com.almuradev.droplet.content.feature.context;

import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;

import java.util.List;

public interface FeatureContext {
  <F> F get(final Class<F> type, final Node node) throws XMLException;

  /**
   * @deprecated use {@link #get(Class, Node)} when you have the node requesting the feature present - this allows
   *   us to track where references are used when a feature has been requested before it has been defined
   */
  @Deprecated
  <F> F get(final Class<F> type, final String id);

  <F> F add(final Class<F> type, final Node node, final F feature);

  /**
   * @deprecated use {@link #add(Class, Node, Object)} when you have the node defining the feature present
   */
  @Deprecated
  <F> F add(final Class<F> type, final String id, final F feature);

  List<XMLException> validate();

  void addParent(final FeatureContext that);
}
