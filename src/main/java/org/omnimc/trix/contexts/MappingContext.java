package org.omnimc.trix.contexts;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.contexts.interfaces.IMappingContext;
import org.omnimc.trix.contexts.interfaces.IMethodContext;
import org.omnimc.trix.hierarchy.HierarchyManager;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingContext implements IMappingContext {
    private final Remapper globalRemapper;
    private String currentClass;

    public MappingContext(Remapper globalRemapper) {
        this.globalRemapper = globalRemapper;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces, ClassVisitor classVisitor) {
        this.currentClass = name;
        classVisitor.visit(version, access, globalRemapper.mapType(name), globalRemapper.mapSignature(signature, false),
                globalRemapper.mapType(superName), interfaces == null ? null : globalRemapper.mapTypes(interfaces));
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor, ClassVisitor classVisitor) {
        String methodDesc = descriptor == null ? null : globalRemapper.mapMethodDesc(descriptor);
        classVisitor.visitOuterClass(globalRemapper.mapType(owner), globalRemapper.mapMethodName(owner, name, methodDesc), methodDesc);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access, ClassVisitor classVisitor) {
        classVisitor.visitInnerClass(globalRemapper.mapType(name),
                outerName == null ? null : globalRemapper.mapType(outerName),
                innerName == null ? null : globalRemapper.mapType(innerName), access);
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass, ClassVisitor classVisitor) {
        classVisitor.visitPermittedSubclass(globalRemapper.mapType(permittedSubclass));
    }

    @Override
    public void visitNestHost(String nestHost, ClassVisitor classVisitor) {
        classVisitor.visitNestHost(globalRemapper.mapType(nestHost));
    }

    @Override
    public void visitNestMember(String nestMember, ClassVisitor classVisitor) {
        classVisitor.visitNestMember(globalRemapper.mapType(nestMember));
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature, ClassVisitor classVisitor) {
        return classVisitor.visitRecordComponent(
                name,
                globalRemapper.mapDesc(descriptor),
                globalRemapper.mapSignature(signature, true));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value, ClassVisitor classVisitor) {
        return classVisitor.visitField(access,
                globalRemapper.mapFieldName(currentClass, name, descriptor),
                globalRemapper.mapDesc(descriptor),
                globalRemapper.mapSignature(signature, true), globalRemapper.mapValue(value));
    }

    @Override
    public IMethodContext visitMethod(int access, String name, String descriptor, String signature, String[] exceptions, ClassVisitor classVisitor) {
        String mappedMethodDesc = globalRemapper.mapMethodDesc(descriptor);
        MethodVisitor methodVisitor = classVisitor.visitMethod(access,
                globalRemapper.mapMethodName(currentClass, name, mappedMethodDesc),
                mappedMethodDesc,
                globalRemapper.mapSignature(signature, false),
                exceptions == null ? null : globalRemapper.mapTypes(exceptions));

        return new MethodContext(globalRemapper, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible, ClassVisitor classVisitor) {
        return classVisitor.visitTypeAnnotation(typeRef, typePath, globalRemapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible, ClassVisitor classVisitor) {
        return classVisitor.visitAnnotation(globalRemapper.mapDesc(descriptor), visible);
    }

}