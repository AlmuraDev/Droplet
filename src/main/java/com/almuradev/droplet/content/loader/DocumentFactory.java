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

import net.kyori.xml.XMLException;
import net.kyori.xml.node.Node;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class DocumentFactory {
  private static final String INCLUDE_ELEMENT_NAME = "include";
  private final SAXBuilder builder = new SAXBuilder();
  private final List<Path> includePaths;

  public DocumentFactory(final List<Path> includePaths) {
    this.includePaths = includePaths;
  }

  public Document read(final Path path) throws XMLException {
    return this.readInternal(path);
  }

  private Document readInternal(final Path path) throws XMLException {
    final Document document;
    try {
      document = this.builder.build(path.toFile());
    } catch(final IOException e) {
      throw new XMLException("Encountered an exception while reading", e);
    } catch(final JDOMException e) {
      throw new XMLException("Encountered an exception while parsing", e);
    }

    this.processChildren(path, document.getRootElement());

    return document;
  }

  private void processChildren(final Path path, final Element parent) throws XMLException {
    for(int i = 0; i < parent.getContentSize(); i++) {
      final Content content = parent.getContent(i);
      if(!(content instanceof Element)) {
        continue;
      }

      final Element child = (Element) content;

      if(child.getName().equals(INCLUDE_ELEMENT_NAME)) {
        parent.setContent(i, this.readInclude(path, child));
        i--;
      } else {
        this.processChildren(path, child);
      }
    }
  }

  private List<Content> readInclude(final Path sourcePath, final Element element) throws XMLException {
    final Path path = Paths.get(Node.of(element).requireAttribute("src").normalizedValue());
    return this.readInclude(sourcePath.getParent(), path);
  }

  private List<Content> readInclude(@Nullable final Path parentPath, final Path include) throws XMLException {
    final Path path = this.findInclude(parentPath, include);
    if(path == null) {
      throw new XMLException("Could not find include: " + include);
    }
    return this.readInternal(path).getRootElement().cloneContent();
  }

  @Nullable
  private Path findInclude(@Nullable final Path parentPath, final Path include) {
    final List<Path> includePaths = new ArrayList<>(this.includePaths);
    if(parentPath != null) {
      includePaths.add(0, parentPath);
    }
    for(final Path includePath : includePaths) {
      final Path path = includePath.resolve(include);
      if(Files.isRegularFile(path)) {
        return path.toAbsolutePath();
      }
    }
    return null;
  }
}
