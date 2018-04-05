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
package com.almuradev.droplet.parser;

import com.google.common.collect.MoreCollectors;
import net.kyori.xml.node.Node;

import java.util.stream.Stream;

public final class Nodes {
  private Nodes() {
  }

  /**
   * Require a single node from the given stream.
   *
   * @param stream the stream
   * @return the node
   * @throws IllegalArgumentException if the stream contains more than one node
   */
  public static Node requireSingle(final Stream<Node> stream) {
    return stream.collect(MoreCollectors.onlyElement());
  }

  public static Node firstNonEmpty(final Node node, final String child) {
    if(!node.value().isEmpty()) {
      return node;
    }
    return node.nodes(child).collect(MoreCollectors.onlyElement());
  }
}
