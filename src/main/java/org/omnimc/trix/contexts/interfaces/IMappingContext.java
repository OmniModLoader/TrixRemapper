package org.omnimc.trix.contexts.interfaces;

import org.objectweb.asm.*;
import org.omnimc.trix.mapping.MappingManager;

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

    MappingManager getMappingManager();
}
