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

package org.omnimc.trix.visitors.mapping;

import org.objectweb.asm.*;
import org.omnimc.trix.contexts.interfaces.IMethodContext;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingMethodVisitor extends MethodVisitor {
    private final IMethodContext methodContext;

    public MappingMethodVisitor(MethodVisitor methodVisitor, IMethodContext methodContext) {
        super(Opcodes.ASM9, methodVisitor);
        this.methodContext = methodContext;
    }


    /**
     * {@inheritDoc}
     *
     * @param name                     the method's name.
     * @param descriptor               the method's descriptor (see {@link Type}).
     * @param bootstrapMethodHandle    the bootstrap method.
     * @param bootstrapMethodArguments the bootstrap method constant arguments. Each argument must be an
     *                                 {@link Integer}, {@link Float}, {@link Long}, {@link Double}, {@link String},
     *                                 {@link Type}, {@link Handle} or {@link ConstantDynamic} value. This method is
     *                                 allowed to modify the content of the array so a caller should expect that this
     *                                 array may change.
     */
    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        methodContext.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, getDelegate(), bootstrapMethodArguments); // todo fix
    }

    /**
     * {@inheritDoc}
     *
     * @param name       the name of a local variable.
     * @param descriptor the type descriptor of this local variable.
     * @param signature  the type signature of this local variable. May be {@literal null} if the local variable type
     *                   does not use generic types.
     * @param start      the first instruction corresponding to the scope of this local variable (inclusive).
     * @param end        the last instruction corresponding to the scope of this local variable (exclusive).
     * @param index      the local variable's index.
     */
    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        methodContext.visitLocalVariable(name, descriptor, signature, start, end, index, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param value the constant to be loaded on the stack. This parameter must be a non null {@link Integer}, a
     *              {@link Float}, a {@link Long}, a {@link Double}, a {@link String}, a {@link Type} of OBJECT or ARRAY
     *              sort for {@code .class} constants, for classes whose version is 49, a {@link Type} of METHOD sort
     *              for MethodType, a {@link Handle} for MethodHandle constants, for classes whose version is 51 or a
     *              {@link ConstantDynamic} for a constant dynamic for classes whose version is 55.
     */
    @Override
    public void visitLdcInsn(Object value) {
        methodContext.visitLdcInsn(value, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param type     the type of this stack map frame. Must be {@link Opcodes#F_NEW} for expanded frames, or
     *                 {@link Opcodes#F_FULL}, {@link Opcodes#F_APPEND}, {@link Opcodes#F_CHOP}, {@link Opcodes#F_SAME}
     *                 or {@link Opcodes#F_APPEND}, {@link Opcodes#F_SAME1} for compressed frames.
     * @param numLocal the number of local variables in the visited frame. Long and double values count for one
     *                 variable.
     * @param local    the local variable types in this frame. This array must not be modified. Primitive types are
     *                 represented by {@link Opcodes#TOP}, {@link Opcodes#INTEGER}, {@link Opcodes#FLOAT},
     *                 {@link Opcodes#LONG}, {@link Opcodes#DOUBLE}, {@link Opcodes#NULL} or
     *                 {@link Opcodes#UNINITIALIZED_THIS} (long and double are represented by a single element).
     *                 Reference types are represented by String objects (representing internal names, see
     *                 {@link Type#getInternalName()}), and uninitialized types by Label objects (this label designates
     *                 the NEW instruction that created this uninitialized value).
     * @param numStack the number of operand stack elements in the visited frame. Long and double values count for one
     *                 stack element.
     * @param stack    the operand stack types in this frame. This array must not be modified. Its content has the same
     *                 format as the "local" array.
     */
    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        methodContext.visitFrame(type, numLocal, local, numStack, stack, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param opcode the opcode of the type instruction to be visited. This opcode is either NEW, ANEWARRAY, CHECKCAST
     *               or INSTANCEOF.
     * @param type   the operand of the instruction to be visited. This operand must be the internal name of an object
     *               or array class (see {@link Type#getInternalName()}).
     */
    @Override
    public void visitTypeInsn(int opcode, String type) {
        methodContext.visitTypeInsn(opcode, type, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param opcode     the opcode of the type instruction to be visited. This opcode is either GETSTATIC, PUTSTATIC,
     *                   GETFIELD or PUTFIELD.
     * @param owner      the internal name of the field's owner class (see {@link Type#getInternalName()}).
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     */
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        methodContext.visitFieldInsn(opcode, owner, name, descriptor, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param opcode      the opcode of the type instruction to be visited. This opcode is either INVOKEVIRTUAL,
     *                    INVOKESPECIAL, INVOKESTATIC or INVOKEINTERFACE.
     * @param owner       the internal name of the method's owner class (see {@link Type#getInternalName()}).
     * @param name        the method's name.
     * @param descriptor  the method's descriptor (see {@link Type}).
     * @param isInterface if the method's owner class is an interface.
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        methodContext.visitMethodInsn(opcode, owner, name, descriptor, isInterface, getDelegate());
    }


    /**
     * {@inheritDoc}
     *
     * @param typeRef    a reference to the annotated type. The sort of this type reference must be
     *                   {@link TypeReference#LOCAL_VARIABLE} or {@link TypeReference#RESOURCE_VARIABLE}. See
     *                   {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or static inner
     *                   type within 'typeRef'. May be {@literal null} if the annotation targets 'typeRef' as a whole.
     * @param start      the fist instructions corresponding to the continuous ranges that make the scope of this local
     *                   variable (inclusive).
     * @param end        the last instructions corresponding to the continuous ranges that make the scope of this local
     *                   variable (exclusive). This array must have the same size as the 'start' array.
     * @param index      the local variable's index in each range. This array must have the same size as the 'start'
     *                   array.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return methodContext.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible, getDelegate());
    }

    /**
     * {@inheritDoc}\
     *
     * @param typeRef    a reference to the annotated type. The sort of this type reference must be
     *                   {@link TypeReference#INSTANCEOF}, {@link TypeReference#NEW},
     *                   {@link TypeReference#CONSTRUCTOR_REFERENCE}, {@link TypeReference#METHOD_REFERENCE},
     *                   {@link TypeReference#CAST}, {@link TypeReference#CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT},
     *                   {@link TypeReference#METHOD_INVOCATION_TYPE_ARGUMENT},
     *                   {@link TypeReference#CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT}, or
     *                   {@link TypeReference#METHOD_REFERENCE_TYPE_ARGUMENT}. See {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or static inner
     *                   type within 'typeRef'. May be {@literal null} if the annotation targets 'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return methodContext.visitInsnAnnotation(typeRef, typePath, descriptor, visible, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param start   the beginning of the exception handler's scope (inclusive).
     * @param end     the end of the exception handler's scope (exclusive).
     * @param handler the beginning of the exception handler's code.
     * @param type    the internal name of the type of exceptions handled by the handler (see
     *                {@link Type#getInternalName()}), or {@literal null} to catch any exceptions (for "finally"
     *                blocks).
     */
    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        methodContext.visitTryCatchBlock(start, end, handler, type, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param typeRef    a reference to the annotated type. The sort of this type reference must be
     *                   {@link TypeReference#METHOD_TYPE_PARAMETER}, {@link TypeReference#METHOD_TYPE_PARAMETER_BOUND},
     *                   {@link TypeReference#METHOD_RETURN}, {@link TypeReference#METHOD_RECEIVER},
     *                   {@link TypeReference#METHOD_FORMAL_PARAMETER} or {@link TypeReference#THROWS}. See
     *                   {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or static inner
     *                   type within 'typeRef'. May be {@literal null} if the annotation targets 'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return methodContext.visitTypeAnnotation(typeRef, typePath, descriptor, visible, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param parameter  the parameter index. This index must be strictly smaller than the number of parameters in the
     *                   method descriptor, and strictly smaller than the parameter count specified in
     *                   {@link #visitAnnotableParameterCount}. Important note: <i>a parameter index i is not required
     *                   to correspond to the i'th parameter descriptor in the method descriptor</i>, in particular in
     *                   case of synthetic parameters (see
     *                   https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.18).
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return methodContext.visitParameterAnnotation(parameter, descriptor, visible, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return methodContext.visitAnnotation(descriptor, visible, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor    an array type descriptor (see {@link Type}).
     * @param numDimensions the number of dimensions of the array to allocate.
     */
    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        methodContext.visitMultiANewArrayInsn(descriptor, numDimensions, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param typeRef    a reference to the annotated type. The sort of this type reference must be
     *                   {@link TypeReference#EXCEPTION_PARAMETER}. See {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or static inner
     *                   type within 'typeRef'. May be {@literal null} if the annotation targets 'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return methodContext.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible, getDelegate());
    }
}