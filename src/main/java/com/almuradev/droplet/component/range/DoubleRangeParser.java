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

import com.almuradev.droplet.parser.ParserException;
import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;
import net.kyori.xml.node.parser.Parser;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class DoubleRangeParser implements Parser<DoubleRange> {
  private final Parser<Double> doubleParser;

  @Inject
  private DoubleRangeParser(final Parser<Double> doubleParser) {
    this.doubleParser = doubleParser;
  }

  @Override
  public DoubleRange throwingParse(final Node node) throws XMLException {
    final Optional<Node> value = node.attribute("value").optional();
    if(value.isPresent()) {
      return new DoubleRangeImpl(this.doubleParser.parse(value.get()));
    }

    final Optional<Node> min = node.attribute("min").optional();
    final Optional<Node> max = node.attribute("max").optional();
    if(min.isPresent() && max.isPresent()) {
      return new DoubleRangeImpl(this.doubleParser.parse(min.get()), this.doubleParser.parse(max.get()));
    }

    try {
      return new DoubleRangeImpl(this.doubleParser.parse(node));
    } catch(final Throwable t) {
      // ignore
    }

    throw new ParserException("Could not throwingParse double range");
  }
}
