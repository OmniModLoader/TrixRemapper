package org.omnimc.trix.contexts;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.contexts.interfaces.IMethodContext;
import org.omnimc.trix.hierarchy.HierarchyManager;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MethodContext implements IMethodContext {
    private final HierarchyManager hierarchyManager;
    private final MethodVisitor parentVisitor;
    private final Remapper globalRemapper;

    public MethodContext(Remapper globalRemapper, HierarchyManager hierarchyManager, MethodVisitor parentVisitor) {
        this.hierarchyManager = hierarchyManager;
        this.parentVisitor = parentVisitor;
        this.globalRemapper = globalRemapper;
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, MethodVisitor methodVisitor, Object... bootstrapMethodArguments) {
        if (bootstrapMethodArguments != null) {

            for (int i = 0; i < bootstrapMethodArguments.length; i++) {
                if (bootstrapMethodArguments[i] instanceof Type) { // this is descriptors
                    bootstrapMethodArguments[i] = globalRemapper.mapValue(bootstrapMethodArguments[i]);
                } else if (bootstrapMethodArguments[i] instanceof Handle) {
                    String owner = ((Handle) bootstrapMethodArguments[i]).getOwner();
                    String desc = ((Handle) bootstrapMethodArguments[i]).getDesc();

                    if (desc.contains("(")) {
                        desc = globalRemapper.mapMethodDesc(desc);
                    } else {
                        desc = globalRemapper.mapDesc(desc);
                    }

                    String handleName = desc.contains("(") ? globalRemapper.mapMethodName(owner, ((Handle) bootstrapMethodArguments[i]).getName(), desc) : globalRemapper.mapFieldName(owner, ((Handle) bootstrapMethodArguments[i]).getName(), null);

                    int tag = ((Handle) bootstrapMethodArguments[i]).getTag();
                    boolean anInterface = ((Handle) bootstrapMethodArguments[i]).isInterface();


                    bootstrapMethodArguments[i] = new Handle(tag, globalRemapper.mapType(owner), handleName, desc, anInterface);
                }

            }
        }

        methodVisitor.visitInvokeDynamicInsn(name, globalRemapper.mapMethodDesc(descriptor), (Handle) globalRemapper.mapValue(bootstrapMethodHandle), bootstrapMethodArguments);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index, MethodVisitor methodVisitor) {
        methodVisitor.visitLocalVariable(name, globalRemapper.mapDesc(descriptor),
                globalRemapper.mapSignature(signature, true), start, end, index);
    }

    @Override
    public void visitLdcInsn(Object value, MethodVisitor methodVisitor) {
        methodVisitor.visitLdcInsn(globalRemapper.mapValue(value));

    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack, MethodVisitor methodVisitor) {
        methodVisitor.visitFrame(type,
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

                String mapType = globalRemapper.mapType((String) frameTypes[i]);
                if (mapType.contains("[")) {
                    mapType = globalRemapper.mapDesc((String) frameTypes[i]);
                }

                remappedFrameTypes[i] = mapType;
            }
        }
        return remappedFrameTypes == null ? frameTypes : remappedFrameTypes;
    }

    @Override
    public void visitTypeInsn(int opcode, String type, MethodVisitor methodVisitor) {
        String remappedType = globalRemapper.mapType(type);
        methodVisitor.visitTypeInsn(opcode, type.contains("[") ? globalRemapper.mapDesc(remappedType) : remappedType);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor, MethodVisitor methodVisitor) {
        String mappedName = globalRemapper.mapFieldName(owner, name, descriptor);

        if (mappedName.equals(name) && hierarchyManager != null) {
            mappedName = hierarchyManager.getFieldName(owner, name, descriptor);
        }

        methodVisitor.visitFieldInsn(opcode, globalRemapper.mapType(owner), mappedName == null ? globalRemapper.mapMethodName(owner, name, descriptor) : mappedName, globalRemapper.mapDesc(descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface, MethodVisitor methodVisitor) {
        String mappedDescriptor = globalRemapper.mapMethodDesc(descriptor);
        String ownersName = owner.replace("[", "");

        if (ownersName.startsWith("L") && ownersName.endsWith(";")) {
            ownersName = ownersName.substring(1, ownersName.length() - 1);
        }

        String remapedName = globalRemapper.mapMethodName(ownersName, name, mappedDescriptor);

        if (remapedName.equals(name) && hierarchyManager != null) {
            remapedName = hierarchyManager.getMethodName(ownersName, name, descriptor);
        }

        if (owner.contains("[")) {
            methodVisitor.visitMethodInsn(opcode, globalRemapper.mapDesc(owner), remapedName == null ? globalRemapper.mapMethodName(ownersName, name, mappedDescriptor) : remapedName, mappedDescriptor, isInterface);
            return;
        }
        methodVisitor.visitMethodInsn(opcode, globalRemapper.mapType(ownersName), remapedName == null ? globalRemapper.mapMethodName(ownersName, name, mappedDescriptor) : remapedName, mappedDescriptor, isInterface);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible, MethodVisitor methodVisitor) {
        return methodVisitor.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, MethodVisitor methodVisitor) {
        return methodVisitor.visitInsnAnnotation(typeRef, typePath, globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type, MethodVisitor methodVisitor) {
        methodVisitor.visitTryCatchBlock(start, end, handler, type == null ? null : globalRemapper.mapType(type));

    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, MethodVisitor methodVisitor) {
        return methodVisitor.visitTypeAnnotation(typeRef, typePath, globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible, MethodVisitor methodVisitor) {
        return methodVisitor.visitParameterAnnotation(parameter, globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible, MethodVisitor methodVisitor) {
        return methodVisitor.visitAnnotation(globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions, MethodVisitor methodVisitor) {
        methodVisitor.visitMultiANewArrayInsn(globalRemapper.mapDesc(descriptor), numDimensions);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, MethodVisitor methodVisitor) {
        return methodVisitor.visitTryCatchAnnotation(typeRef, typePath, globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public MethodVisitor getParentVisitor() {
        return parentVisitor;
    }
}