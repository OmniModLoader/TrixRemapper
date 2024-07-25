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