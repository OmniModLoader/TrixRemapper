package org.omnimc.trix.mapping;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;
import org.omnimc.trix.contexts.interfaces.IMappingContext;
import org.omnimc.trix.visitors.mapping.MappingClassVisitor;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingChange implements IClassChange {
    private final IMappingContext mappingContext;

    public MappingChange(IMappingContext mappingContext) {
        this.mappingContext = mappingContext;
    }

    @Override
    public ClassFile applyChange(String name, byte[] classBytes) {
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        MappingClassVisitor remappingVisitor = new MappingClassVisitor(writer, mappingContext);
        reader.accept(remappingVisitor, ClassReader.EXPAND_FRAMES);

        if (name.contains(".class")) {
            name = name.replace(".class", "");
        }

        return new ClassFile(name, writer.toByteArray());
    }
}