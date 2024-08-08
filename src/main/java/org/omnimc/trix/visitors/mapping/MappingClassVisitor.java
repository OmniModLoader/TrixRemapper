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
import org.omnimc.trix.contexts.interfaces.IMappingContext;
import org.omnimc.trix.contexts.interfaces.IMethodContext;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingClassVisitor extends ClassVisitor {

    private final IMappingContext mappingContext;

    public MappingClassVisitor(ClassVisitor classVisitor, IMappingContext mappingContext) {
        super(Opcodes.ASM9, classVisitor);
        this.mappingContext = mappingContext;
    }


    /* Class changes */

    /**
     * {@inheritDoc}
     *
     * @param version    the class version. The minor version is stored in the 16 most significant bits, and the major
     *                   version in the 16 least significant bits.
     * @param access     the class's access flags (see {@link Opcodes}). This parameter also indicates if the class is
     *                   deprecated {@link Opcodes#ACC_DEPRECATED} or a record {@link Opcodes#ACC_RECORD}.
     * @param name       the internal name of the class (see {@link Type#getInternalName()}).
     * @param signature  the signature of this class. May be {@literal null} if the class is not a generic one, and does
     *                   not extend or implement generic classes or interfaces.
     * @param superName  the internal of name of the super class (see {@link Type#getInternalName()}). For interfaces,
     *                   the super class is {@link Object}. May be {@literal null}, but only for the {@link Object}
     *                   class.
     * @param interfaces the internal names of the class's interfaces (see {@link Type#getInternalName()}). May be
     *                   {@literal null}.
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        mappingContext.visit(version, access, name, signature, superName, interfaces, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param owner      internal name of the enclosing class of the class (see {@link Type#getInternalName()}).
     * @param name       the name of the method that contains the class, or {@literal null} if the class is not enclosed
     *                   in a method or constructor of its enclosing class (e.g. if it is enclosed in an instance
     *                   initializer, static initializer, instance variable initializer, or class variable
     *                   initializer).
     * @param descriptor the descriptor of the method that contains the class, or {@literal null} if the class is not
     *                   enclosed in a method or constructor of its enclosing class (e.g. if it is enclosed in an
     *                   instance initializer, static initializer, instance variable initializer, or class variable
     *                   initializer).
     */
    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        mappingContext.visitOuterClass(owner, name, descriptor, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param name      the internal name of C (see {@link Type#getInternalName()}).
     * @param outerName the internal name of the class or interface C is a member of (see
     *                  {@link Type#getInternalName()}). Must be {@literal null} if C is not the member of a class or
     *                  interface (e.g. for local or anonymous classes).
     * @param innerName the (simple) name of C. Must be {@literal null} for anonymous inner classes.
     * @param access    the access flags of C originally declared in the source code from which this class was
     *                  compiled.
     */
    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        mappingContext.visitInnerClass(name, outerName, innerName, access, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param permittedSubclass the internal name of a permitted subclass (see {@link Type#getInternalName()}).
     */
    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        mappingContext.visitPermittedSubclass(permittedSubclass, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param nestHost the internal name of the host class of the nest (see {@link Type#getInternalName()}).
     */
    @Override
    public void visitNestHost(String nestHost) {
        mappingContext.visitNestHost(nestHost, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param nestMember the internal name of a nest member (see {@link Type#getInternalName()}).
     */
    @Override
    public void visitNestMember(String nestMember) {
        mappingContext.visitNestMember(nestMember, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param name       the record component name.
     * @param descriptor the record component descriptor (see {@link Type}).
     * @param signature  the record component signature. May be {@literal null} if the record component type does not
     *                   use generic types.
     * @return {@linkplain RecordComponentVisitor}
     */
    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        return mappingContext.visitRecordComponent(name, descriptor, signature, getDelegate());
    }


    /* Field and Method Changes */

    /**
     * {@inheritDoc}
     *
     * @param access     the field's access flags (see {@link Opcodes}). This parameter also indicates if the field is
     *                   synthetic and/or deprecated.
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     * @param signature  the field's signature. May be {@literal null} if the field's type does not use generic types.
     * @param value      the field's initial value. This parameter, which may be {@literal null} if the field does not
     *                   have an initial value, must be an {@link Integer}, a {@link Float}, a {@link Long}, a
     *                   {@link Double} or a {@link String} (for {@code int}, {@code float}, {@code long} or
     *                   {@code String} fields respectively). <i>This parameter is only used for static fields</i>. Its
     *                   value is ignored for non static fields, which must be initialized through bytecode instructions
     *                   in constructors or methods.
     * @return {@linkplain FieldVisitor}
     */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return mappingContext.visitField(access, name, descriptor, signature, value, getDelegate());
    }

    /**
     * {@inheritDoc}
     *
     * @param access     the method's access flags (see {@link Opcodes}). This parameter also indicates if the method is
     *                   synthetic and/or deprecated.
     * @param name       the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature  the method's signature. May be {@literal null} if the method parameters, return type and
     *                   exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link Type#getInternalName()}). May
     *                   be {@literal null}.
     * @return {@linkplain MappingMethodVisitor}
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        IMethodContext methodContext = mappingContext.visitMethod(access, name, descriptor, signature, exceptions, getDelegate());
        return new MappingMethodVisitor(methodContext.getParentVisitor(), methodContext);
    }


    /* Annotation Changes */

    /**
     * {@inheritDoc}
     *
     * @param typeRef    a reference to the annotated type. The sort of this type reference must be
     *                   {@link TypeReference#CLASS_TYPE_PARAMETER}, {@link TypeReference#CLASS_TYPE_PARAMETER_BOUND} or
     *                   {@link TypeReference#CLASS_EXTENDS}. See {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or static inner
     *                   type within 'typeRef'. May be {@literal null} if the annotation targets 'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return {@linkplain AnnotationVisitor}
     */
    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return mappingContext.visitTypeAnnotation(typeRef, typePath, descriptor, visible, getDelegate());
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
        return mappingContext.visitAnnotation(descriptor, visible, getDelegate());
    }
}