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
package com.almuradev.droplet.content.type;

import com.google.common.reflect.TypeToken;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public interface ContentType {
  String id();

  Path path(final Path parent);

  abstract class Impl implements ContentType {
    protected final String id;
    protected final List<String> path;

    public Impl(final String id) {
      this(id, Collections.singletonList(id));
    }

    public Impl(final String id, final List<String> path) {
      this.id = id;
      this.path = path;
    }

    @Override
    public String id() {
      return this.id;
    }

    @Override
    public Path path(Path parent) {
      for(final String path : this.path) {
        parent = parent.resolve(path);
      }
      return parent;
    }

    @Override
    public String toString() {
      return this.getClass().getName() + '{' + this.id + '=' + this.path + '}';
    }
  }

  interface Child extends ContentType {
    class Impl extends ContentType.Impl implements Child {
      public Impl(final String id) {
        super(id);
      }

      public Impl(final String id, final List<String> path) {
        super(id, path);
      }
    }
  }

  interface Root<C extends Child> extends ContentType {
    TypeToken<C> childType();

    String rootElement();

    abstract class Impl<C extends Child> extends ContentType.Impl implements Root<C> {
      private final TypeToken<C> childType;
      private final String rootElement;

      public Impl(final String id, final String rootElement, final TypeToken<C> childType) {
        super(id);
        this.childType = childType;
        this.rootElement = rootElement;
      }

      public Impl(final String id, final String rootElement, final List<String> path, final TypeToken<C> childType) {
        super(id, path);
        this.childType = childType;
        this.rootElement = rootElement;
      }

      @Override
      public TypeToken<C> childType() {
        return this.childType;
      }

      @Override
      public String rootElement() {
        return this.rootElement;
      }
    }
  }
}
