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
public interface IMethodContext {

    void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, MethodVisitor methodVisitor, Object... bootstrapMethodArguments);

    void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index, MethodVisitor methodVisitor);

    void visitLdcInsn(Object value, MethodVisitor methodVisitor);

    void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack, MethodVisitor methodVisitor);

    void visitTypeInsn(int opcode, String type, MethodVisitor methodVisitor);

    void visitFieldInsn(int opcode, String owner, String name, String descriptor, MethodVisitor methodVisitor);

    void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface, MethodVisitor methodVisitor);

    AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible, MethodVisitor methodVisitor);

    AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, MethodVisitor methodVisitor);

    void visitTryCatchBlock(Label start, Label end, Label handler, String type, MethodVisitor methodVisitor);

    AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, MethodVisitor methodVisitor);

    AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible, MethodVisitor methodVisitor);

    AnnotationVisitor visitAnnotation(String descriptor, boolean visible, MethodVisitor methodVisitor);

    void visitMultiANewArrayInsn(String descriptor, int numDimensions, MethodVisitor methodVisitor);

    AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, MethodVisitor methodVisitor);

    MethodVisitor getParentVisitor();
}