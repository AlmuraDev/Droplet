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

public class FoundContentImpl<R extends ContentType.Root<C>, C extends ContentType.Child> implements FoundContent<R, C> {
  private final ListMultimap<C, FoundContentEntry<R, C>> entries;
  @Nullable private List<FoundEntry> typeIncludes;
  @Nullable private ListMultimap<C, FoundEntry> childIncludes;

  public FoundContentImpl() {
    this(ArrayListMultimap.create(), new ArrayList<>(), ArrayListMultimap.create());
  }

  public FoundContentImpl(final ListMultimap<C, FoundContentEntry<R, C>> entries, @Nullable final List<FoundEntry> typeIncludes, @Nullable final ListMultimap<C, FoundEntry> childIncludes) {
    this.entries = entries;
    this.typeIncludes = typeIncludes;
    this.childIncludes = childIncludes;
  }

  @Override
  public List<FoundContentEntry<R, C>> entries(final C type) {
    return this.entries.get(type).stream().filter(FoundEntry::valid).collect(Collectors.toList());
  }

  @Override
  public List<FoundContentEntry<R, C>> entries() {
    return Multimaps.asMap(this.entries).values().stream().flatMap(Collection::stream)
      .filter(FoundEntry::valid)
      .collect(Collectors.toList());
  }

  @Override
  public void pushEntry(final FoundContentEntry<R, C> entry) {
    this.entries.put(entry.childType(), entry);
  }

  @Override
  public Optional<List<FoundEntry>> typeIncludes() {
    return Optional.ofNullable(this.typeIncludes);
  }

  @Override
  public Optional<ListMultimap<C, FoundEntry>> childIncludes() {
    return Optional.ofNullable(this.childIncludes);
  }

  @Override
  public void pushTypeInclude(final FoundEntry entry) {
    if(this.typeIncludes == null) {
      this.typeIncludes = new ArrayList<>();
    }
    this.typeIncludes.add(entry);
  }

  @Override
  public void pushChildInclude(final C type, final FoundEntry entry) {
    if(this.childIncludes == null) {
      this.childIncludes = ArrayListMultimap.create();
    }
    this.childIncludes.put(type, entry);
  }
}
