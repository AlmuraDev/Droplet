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
package com.almuradev.droplet.content.loader.finder;

import com.almuradev.droplet.content.feature.context.FeatureContext;
import com.almuradev.droplet.content.type.Content;
import com.almuradev.droplet.content.type.ContentBuilder;
import com.almuradev.droplet.content.type.ContentType;
import com.almuradev.droplet.registry.RegistryKey;
import org.jdom2.Element;

import java.nio.file.Path;
import java.util.List;

public final class FoundContent<R extends ContentType.Root<C>, C extends ContentType.Child> {
  private final List<Entry<R, C>> entries;

  public FoundContent(final List<Entry<R, C>> entries) {
    this.entries = entries;
  }

  public List<Entry<R, C>> entries() {
    return this.entries;
  }

  public void offer(final Entry<?, C> entry) {
    this.entries.add((Entry<R, C>) entry);
  }

  public interface Entry<R extends ContentType.Root<C>, C extends ContentType.Child> {
    String namespace();

    RegistryKey key();

    R rootType();

    C childType();

    Path absolutePath();

    Element rootElement();

    FeatureContext context();

    ContentBuilder builder();

    Content result();

    default <B> B result(final Class<B> resultType) {
      return resultType.cast(this.result());
    }
  }
}
