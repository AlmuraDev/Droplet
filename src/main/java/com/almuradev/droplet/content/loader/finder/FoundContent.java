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

import com.almuradev.droplet.content.type.ContentType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FoundContent<R extends ContentType.Root<C>, C extends ContentType.Child> {
  private final ListMultimap<C, FoundContentEntry<R, C>> entries;
  @Nullable private final List<FoundEntry> typeIncludes;
  @Nullable private final ListMultimap<C, FoundEntry> childIncludes;

  public FoundContent() {
    this(ArrayListMultimap.create(), new ArrayList<>(), ArrayListMultimap.create());
  }

  public FoundContent(final ListMultimap<C, FoundContentEntry<R, C>> entries, @Nullable final List<FoundEntry> typeIncludes, @Nullable final ListMultimap<C, FoundEntry> childIncludes) {
    this.entries = entries;
    this.typeIncludes = typeIncludes;
    this.childIncludes = childIncludes;
  }

  public List<FoundContentEntry<R, C>> entries(final C type) {
    return this.entries.get(type);
  }

  public List<FoundContentEntry<R, C>> entries() {
    return Multimaps.asMap(this.entries).values().stream().flatMap(Collection::stream).collect(Collectors.toList());
  }

  public Optional<List<FoundEntry>> typeIncludes() {
    return Optional.ofNullable(this.typeIncludes);
  }

  public Optional<ListMultimap<C, FoundEntry>> childIncludes() {
    return Optional.ofNullable(this.childIncludes);
  }

  public void offer(final FoundContentEntry<?, C> entry) {
    this.entries.put(entry.childType(), (FoundContentEntry<R, C>) entry);
  }
}
