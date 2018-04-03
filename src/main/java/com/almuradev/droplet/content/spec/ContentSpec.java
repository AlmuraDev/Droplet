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
package com.almuradev.droplet.content.spec;

public interface ContentSpec {
  /**
   * The current droplet specification.
   */
  ContentSpec CURRENT = new ContentSpecImpl(1, 0, 0);

  static ContentSpec parse(final String string) {
    final String[] parts = string.split("\\.");
    if(parts.length != 3) {
      throw new IllegalArgumentException("Expected 'major.minor.patch', got '" + string + '\'');
    }
    return new ContentSpecImpl(
      Integer.parseInt(parts[0]),
      Integer.parseInt(parts[1]),
      Integer.parseInt(parts[2])
    );
  }

  int major();

  int minor();

  int patch();

  default boolean atLeast(final ContentSpec that) {
    return this.major() >= that.major()
      && this.minor() >= that.minor()
      && this.patch() >= that.patch();
  }
}
