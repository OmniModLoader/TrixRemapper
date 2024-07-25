package org.omnimc.trix.contexts.interfaces;

import org.objectweb.asm.Opcodes;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IHierarchyContext {

    void visit(int access, String name, String superName, String[] interfaces);

    void visitMethod(int access, String name, String descriptor);

    void visitField(int access, String name, String descriptor);

    void visitEnd();

    default boolean isPrivateAccess(int access) {
        return (access & Opcodes.ACC_PRIVATE) != 0;
    }
}