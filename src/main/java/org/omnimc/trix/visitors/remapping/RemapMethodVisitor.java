package org.omnimc.trix.visitors.remapping;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.managers.HierarchyManager;
import org.omnimc.trix.managers.MappingManager;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class RemapMethodVisitor extends MethodVisitor {
    private final HierarchyManager hierarchyManager;
    private final Remapper remapper;

    public RemapMethodVisitor(MethodVisitor methodVisitor, MappingManager mappingManager, HierarchyManager hierarchyManager) {
        super(Opcodes.ASM9, methodVisitor);
        this.hierarchyManager = hierarchyManager;
        this.remapper = mappingManager.getRemapper();
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
        if (bootstrapMethodArguments != null) {

            for (int i = 0; i < bootstrapMethodArguments.length; i++) {
                if (bootstrapMethodArguments[i] instanceof Type) { // this is descriptors
                    bootstrapMethodArguments[i] = remapper.mapValue(bootstrapMethodArguments[i]);
                } else if (bootstrapMethodArguments[i] instanceof Handle) {
                    String owner = ((Handle) bootstrapMethodArguments[i]).getOwner();
                    String desc = ((Handle) bootstrapMethodArguments[i]).getDesc();

                    if (desc.contains("(")) {
                        desc = remapper.mapMethodDesc(desc);
                    } else {
                        desc = remapper.mapDesc(desc);
                    }

                    String handleName = desc.contains("(") ? remapper.mapMethodName(owner, ((Handle) bootstrapMethodArguments[i]).getName(), desc) : remapper.mapFieldName(owner, ((Handle) bootstrapMethodArguments[i]).getName(), null);

                    int tag = ((Handle) bootstrapMethodArguments[i]).getTag();
                    boolean anInterface = ((Handle) bootstrapMethodArguments[i]).isInterface();


                    bootstrapMethodArguments[i] = new Handle(tag, remapper.mapType(owner), handleName, desc, anInterface);
                }

            }
        }

        super.visitInvokeDynamicInsn(name, remapper.mapMethodDesc(descriptor), (Handle) remapper.mapValue(bootstrapMethodHandle), bootstrapMethodArguments);
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
        super.visitLocalVariable(name, remapper.mapDesc(descriptor), remapper.mapSignature(signature, true), start, end, index);
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
        super.visitLdcInsn(remapper.mapValue(value));
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
        super.visitFrame(type,
                numLocal,
                remapFrameTypes(numLocal, local),
                numStack,
                remapFrameTypes(numStack, stack));
    }

    private Object[] remapFrameTypes(final int numTypes, final Object[] frameTypes) {
        if (frameTypes == null) {
            return null;
        }
        Object[] remappedFrameTypes = null;
        for (int i = 0; i < numTypes; ++i) {
            if (frameTypes[i] instanceof String) {
                if (remappedFrameTypes == null) {
                    remappedFrameTypes = new Object[numTypes];
                    System.arraycopy(frameTypes, 0, remappedFrameTypes, 0, numTypes);
                }

                String mapType = remapper.mapType((String) frameTypes[i]);
                if (mapType.contains("[")) {
                    mapType = remapper.mapDesc((String) frameTypes[i]);
                }

                remappedFrameTypes[i] = mapType;
            }
        }
        return remappedFrameTypes == null ? frameTypes : remappedFrameTypes;
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
        String remappedType = remapper.mapType(type);
        super.visitTypeInsn(opcode, type.contains("[") ? remapper.mapDesc(remappedType) : remappedType);
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
        String mappedName = remapper.mapFieldName(owner, name, descriptor);
        String mappedOwner = remapper.mapType(owner);
        String mappedDesc = remapper.mapDesc(descriptor);

        if (mappedName.equals(name)) {
            mappedName = hierarchyManager.getFieldName(owner, name, descriptor);
        }

        if (mappedName == null) {
            mappedName = remapper.mapMethodName(owner, name, descriptor);
        }

        super.visitFieldInsn(opcode, mappedOwner, mappedName, mappedDesc);
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
        String mappedDescriptor = remapper.mapMethodDesc(descriptor);
        String ownersName = owner.replace("[", "");

        if (ownersName.startsWith("L") && ownersName.endsWith(";")) {
            ownersName = ownersName.substring(1, ownersName.length() - 1);
        }

        String remapedName = remapper.mapMethodName(ownersName, name, mappedDescriptor);

        if (remapedName.equals(name)) {
            remapedName = hierarchyManager.getMethodName(ownersName, name, descriptor);
        }

        if (owner.contains("[")) {
            super.visitMethodInsn(opcode, remapper.mapDesc(owner), remapedName == null ? remapper.mapMethodName(ownersName, name, mappedDescriptor) : remapedName, mappedDescriptor, isInterface);
            return;
        }
        super.visitMethodInsn(opcode, remapper.mapType(ownersName), remapedName == null ? remapper.mapMethodName(ownersName, name, mappedDescriptor) : remapedName, mappedDescriptor, isInterface);
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
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, remapper.mapDesc(descriptor), visible);
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
        return super.visitInsnAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
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
        super.visitTryCatchBlock(start, end, handler, type == null ? null : remapper.mapType(type));
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
        return super.visitTypeAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
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
        return super.visitParameterAnnotation(parameter, remapper.mapDesc(descriptor), visible);
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
        return super.visitAnnotation(remapper.mapDesc(descriptor), visible);
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor    an array type descriptor (see {@link Type}).
     * @param numDimensions the number of dimensions of the array to allocate.
     */
    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        super.visitMultiANewArrayInsn(remapper.mapDesc(descriptor), numDimensions);
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
        return super.visitTryCatchAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    }
}
