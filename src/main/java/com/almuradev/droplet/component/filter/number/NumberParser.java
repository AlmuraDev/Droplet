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

import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;
import net.kyori.xml.node.parser.Parser;
import net.kyori.xml.node.parser.PrimitiveParser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class NumberParser implements PrimitiveParser<Number> {
  @Inject private Parser<Byte> byteParser;
  @Inject private Parser<Double> doubleParser;
  @Inject private Parser<Float> floatParser;
  @Inject private Parser<Integer> intParser;
  @Inject private Parser<Long> longParser;
  @Inject private Parser<Short> shortParser;

  @Override
  public Number throwingParse(final Node node, final String string) throws XMLException {
    if(string.isEmpty()) {
      throw new XMLException("empty number, got empty string");
    }
    switch(string.charAt(string.length() - 1)) {
      case 'b': return this.byteParser.parse(node);
      case 'd': return this.doubleParser.parse(node);
      case 'f': return this.floatParser.parse(node);
      case 'i': return this.intParser.parse(node);
      case 'l': return this.longParser.parse(node);
      case 's': return this.shortParser.parse(node);
    }
    if(string.indexOf('.') > 0) {
      return this.doubleParser.parse(node);
    }
    return this.intParser.parse(node);
  }
}
