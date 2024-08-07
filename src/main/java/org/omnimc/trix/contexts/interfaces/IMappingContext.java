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

package org.omnimc.trix.contexts.interfaces;

import org.objectweb.asm.*;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IMappingContext {
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces, ClassVisitor classVisitor);

    void visitOuterClass(String owner, String name, String descriptor, ClassVisitor classVisitor);

    void visitInnerClass(String name, String outerName, String innerName, int access, ClassVisitor classVisitor);

    void visitPermittedSubclass(String permittedSubclass, ClassVisitor classVisitor);

    void visitNestHost(String nestHost, ClassVisitor classVisitor);

    void visitNestMember(String nestMember, ClassVisitor classVisitor);

    RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature, ClassVisitor classVisitor);

    FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value, ClassVisitor classVisitor);

    IMethodContext visitMethod(int access, String name, String descriptor, String signature, String[] exceptions, ClassVisitor classVisitor);

    AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, ClassVisitor classVisitor);

    AnnotationVisitor visitAnnotation(String descriptor, boolean visible, ClassVisitor classVisitor);
}