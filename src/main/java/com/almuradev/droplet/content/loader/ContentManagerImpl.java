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
package com.almuradev.droplet.content.loader;

import net.kyori.membrane.facet.Enableable;
import org.slf4j.Logger;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ContentManagerImpl implements ContentManager, Enableable {
  private final Logger logger;
  private final Set<RootContentLoader> rootLoaders;

  @Inject
  private ContentManagerImpl(final Logger logger, final Set<RootContentLoader> rootLoaders) {
    this.logger = logger;
    this.rootLoaders = rootLoaders;
  }

  @Override
  public void enable() {
    this.logger.debug("Discovering content...");
    this.rootLoaders.forEach(RootContentLoader::discover);
    this.logger.debug("Parsing content...");
    this.rootLoaders.forEach(RootContentLoader::parse);
    this.logger.debug("Loading content...");
    this.rootLoaders.forEach(RootContentLoader::queue);
  }

  @Override
  public void disable() {

  }
}
