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

import com.google.common.collect.MoreCollectors;
import net.kyori.feature.FeatureDefinition;
import net.kyori.lunar.Optionals;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeatureContextImpl extends net.kyori.feature.FeatureDefinitionContextImpl implements FeatureContext {
  private final List<FeatureContextImpl> parents = new ArrayList<>();

  @Override
  public <D extends FeatureDefinition> @NonNull D get(final @NonNull Class<D> type, final @NonNull String id) {
    return (D) Optionals
      .first(
        Optional.ofNullable(this.byId.get(type, id)),
        this.parents.stream()
          .map(parent -> parent.byId.get(type, id))
          .collect(MoreCollectors.toOptional())
      )
      .<FeatureDefinition>map(Entry::get)
      .orElseGet(() -> super.get(type, id));
  }

  @Override
  public void addParent(final FeatureContext that) {
    this.parents.add((FeatureContextImpl) that);
  }
}
