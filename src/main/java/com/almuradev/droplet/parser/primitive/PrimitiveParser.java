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
package com.almuradev.droplet.parser.primitive;

import com.almuradev.droplet.parser.Parser;
import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;

/**
 * Parses a {@link Node}'s {@link Node#value() value} into {@code T}.
 *
 * @param <T> the parsed type
 */
public interface PrimitiveParser<T> extends Parser<T> {
  @Override
  default T throwingParse(final Node node) throws XMLException {
    return this.parse(node, node.value());
  }

  /**
   * Parses a a {@link Node}'s {@link Node#value() value} into {@code T}.
   *
   * @param node the node
   * @param string the node value
   * @return the parsed value
   * @throws XMLException if an exception occurred while parsing
   */
  T parse(final Node node, final String string) throws XMLException;
}
