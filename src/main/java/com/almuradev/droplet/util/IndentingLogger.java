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
package com.almuradev.droplet.util;

import org.slf4j.Logger;

import java.util.Stack;

public class IndentingLogger implements AutoCloseable {
  private final Logger logger;
  private final Stack<Entry> stashes = new Stack<>();
  private Entry current;

  public IndentingLogger(final Logger logger) {
    this(logger, 0);
  }

  public IndentingLogger(final Logger logger, final int startLevel) {
    this.logger = logger;
    this.current = new Entry(startLevel);
  }

  public IndentingLogger push() {
    this.stashes.add(this.current);
    this.current = new Entry(this.current.level + 1);
    return this;
  }

  public void push(final Runnable runnable) {
    this.push();
    try {
      runnable.run();
    } finally {
      this.pop();
    }
  }

  @Override
  public void close() {
    this.pop();
  }

  private void pop() {
    this.current = this.stashes.pop();
  }

  public void debug(final String message) {
    this.logger.debug(this.indent() + message);
  }

  public void debug(final String message, final Object... args) {
    this.logger.debug(this.indent() + message, args);
  }

  public void error(final String message) {
    this.logger.error(this.indent() + message);
  }

  public void error(final String message, final Object... args) {
    this.logger.error(this.indent() + message, args);
  }

  private String indent() {
    return Logging.indent(this.current.level);
  }

  private class Entry {
    final int level;

    public Entry(int level) {
      this.level = level;
    }
  }
}
