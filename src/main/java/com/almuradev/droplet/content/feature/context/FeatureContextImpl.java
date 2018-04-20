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
import net.kyori.fragment.feature.Feature;
import net.kyori.fragment.feature.context.FeatureContextEntry;
import net.kyori.lunar.Optionals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeatureContextImpl extends net.kyori.fragment.feature.context.FeatureContextImpl implements FeatureContext {
  private final List<FeatureContextImpl> parents = new ArrayList<>();

  @Override
  protected <F extends Feature> FeatureContextEntry<F> feature(Class<F> type, String id) {
    return (FeatureContextEntry<F>) Optionals.first(
      Optional.ofNullable(this.features.get(id)),
      this.parents.stream().map(parent -> parent.features.get(id)).collect(MoreCollectors.toOptional())
    ).orElseGet(() -> super.feature(type, id));
  }

  @Override
  public void addParent(final FeatureContext that) {
    this.parents.add((FeatureContextImpl) that);
  }
}
