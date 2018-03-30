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

import net.kyori.xml.node.AttributeNode;
import net.kyori.xml.node.ElementNode;
import net.kyori.xml.node.Node;
import org.jdom2.Attribute;
import org.jdom2.Element;

public interface FeatureId {
  boolean auto();

  String get();

  static FeatureId from(final Node node) {
    return node.attribute("id").map(Node::value).map(n -> new Impl(n, false)).orElseGet(() -> {
      final Stack stack = new Stack();
      stack.append(node);
      return new Impl(stack.toString(), true);
    });
  }

  class Impl implements FeatureId {
    private final String id;
    private final boolean auto;

    public Impl(final String id, final boolean auto) {
      this.id = id;
      this.auto = auto;
    }

    @Override
    public boolean auto() {
      return this.auto;
    }

    @Override
    public String get() {
      return this.id;
    }
  }

  final class Stack {
    private final StringBuilder sb = new StringBuilder();
    private boolean empty = true;

    void append(final Node node) {
      if(node instanceof ElementNode) {
        this.append(((ElementNode) node).element());
      } else if(node instanceof AttributeNode) {
        this.append(((AttributeNode) node).attribute());
      }
    }

    private void append(final Element element) {
      final Element parent = element.getParentElement();
      if(parent != null && parent != parent.getDocument().getRootElement()) {
        this.append(parent);
      }

      this.push(element.getName());
    }

    private void append(final Attribute attribute) {
      if(attribute.getParent() != null) {
        this.append(attribute.getParent());
      }
      this.push(attribute.getName());
    }

    private void push(final String string) {
      if(!this.empty) {
        this.sb.append('-');
      }
      this.empty = false;
      this.sb.append(string);
    }

    @Override
    public String toString() {
      return this.sb.toString();
    }
  }
}
