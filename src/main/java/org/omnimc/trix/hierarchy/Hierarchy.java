/*
 * MIT License
 *
 * Copyright (c) 2024 OmniMC
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

package org.omnimc.trix.hierarchy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.omnimc.trix.contexts.MappingContext;
import org.omnimc.trix.visitors.mapping.MappingClassVisitor;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class Hierarchy {
    private final ClassReader reader;
    private final int api;

    private ClassWriter writer;

    public Hierarchy(ClassReader reader) {
        this(reader, Opcodes.ASM9);
    }

    public Hierarchy(ClassReader reader, int api) {
        this.reader = reader;
        this.api = api;
    }

    public Hierarchy setWriterCode(int classWriterCode) {
        if (writer == null) {
            writer = new ClassWriter(classWriterCode);
        }
        return this;
    }

    public Hierarchy remap(Remapper remapper, int readerCode) {
        if (writer == null) {
            return this;
        }

        reader.accept(new MappingClassVisitor(writer, new MappingContext(remapper)), readerCode);
        return this;
    }

    public ClassNode getClassNode(int parsingOptions) {
        ClassNode node = new ClassNode(api);
        reader.accept(node, parsingOptions);
        return node;
    }

    public byte[] getClassBytes() {
        return writer.toByteArray();
    }

    public static Hierarchy getHierarchy(ClassReader reader) {
        return new Hierarchy(reader);
    }

    public static Hierarchy getHierarchy(ClassReader reader, int api) {
        return new Hierarchy(reader, api);
    }
}