package org.omnimc.trix.visitors.hierarchy;

import org.objectweb.asm.Type;
import org.omnimc.trix.file.ClassInfo;
import org.omnimc.trix.managers.HierarchyManager;
import org.omnimc.trix.managers.MappingManager;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyVisitor extends ClassVisitor {
    private final MappingManager mappingManager;
    private final HierarchyManager hierarchyManager;
    private ClassInfo file;

    public HierarchyVisitor(MappingManager mappingManager, HierarchyManager hierarchyManager) {
        super(Opcodes.ASM9);
        this.mappingManager = mappingManager;
        this.hierarchyManager = hierarchyManager;
    }

    /**
     * {@inheritDoc}
     *
     * @param version the class version. The minor version is stored in the 16 most significant bits,
     *     and the major version in the 16 least significant bits.
     * @param access the class's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the class is deprecated {@link Opcodes#ACC_DEPRECATED} or a record {@link
     *     Opcodes#ACC_RECORD}.
     * @param name the internal name of the class (see {@link Type#getInternalName()}).
     * @param signature the signature of this class. May be {@literal null} if the class is not a
     *     generic one, and does not extend or implement generic classes or interfaces.
     * @param superName the internal of name of the super class (see {@link Type#getInternalName()}).
     *     For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     *     {@link Object} class.
     * @param interfaces the internal names of the class's interfaces (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.file = new ClassInfo(name);

        file.addDependentClass(superName);

        if (interfaces != null) {
            for (String anInterface : interfaces) {
                file.addDependentClass(anInterface);
            }
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     * {@inheritDoc}
     *
     * @param access the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the method is synthetic and/or deprecated.
     * @param name the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature the method's signature. May be {@literal null} if the method parameters,
     *     return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     * @return {@linkplain MethodVisitor}
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        String methodName = mappingManager.getRemapper().mapMethodName(file.getClassName(), name, mappingManager.getRemapper().mapMethodDesc(descriptor));

        this.file.addMethod(name + descriptor, methodName);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    /**
     * {@inheritDoc}
     *
     * @param access the field's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the field is synthetic and/or deprecated.
     * @param name the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     * @param signature the field's signature. May be {@literal null} if the field's type does not use
     *     generic types.
     * @param value the field's initial value. This parameter, which may be {@literal null} if the
     *     field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *     Long}, a {@link Double} or a {@link String} (for {@code int}, {@code float}, {@code long}
     *     or {@code String} fields respectively). <i>This parameter is only used for static
     *     fields</i>. Its value is ignored for non static fields, which must be initialized through
     *     bytecode instructions in constructors or methods.
     * @return {@linkplain FieldVisitor}
     */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            return super.visitField(access, name, descriptor, signature, value);
        }

        this.file.addField(name + descriptor, mappingManager.getFieldName(file.getClassName(), name));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        hierarchyManager.addClassFile(file.getClassName(), file);
        super.visitEnd();
    }
}
