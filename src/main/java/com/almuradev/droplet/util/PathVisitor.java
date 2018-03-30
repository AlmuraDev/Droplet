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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * An abstract implementation of a {@link FileVisitor} to allow overriding only what is needed.
 */
public interface PathVisitor extends FileVisitor<Path> {
  @Override
  default FileVisitResult preVisitDirectory(final Path directory, final BasicFileAttributes attributes) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  default FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  default FileVisitResult visitFileFailed(final Path file, final IOException exception) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  default FileVisitResult postVisitDirectory(final Path directory, final IOException exception) throws IOException {
    return FileVisitResult.CONTINUE;
  }
}
