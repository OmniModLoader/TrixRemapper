package org.omnimc.trix.changes;

import org.omnimc.trix.managers.HierarchyManager;
import org.omnimc.trix.managers.MappingManager;
import org.omnimc.trix.visitors.remapping.RemappingVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class RemappingChange implements IClassChange {
    private final MappingManager mappingManager;
    private final HierarchyManager hierarchyManager;

    public RemappingChange(MappingManager mappingManager, HierarchyManager hierarchyManager) {
        this.mappingManager = mappingManager;
        this.hierarchyManager = hierarchyManager;
    }

    @Override
    public ClassFile applyChange(String name, byte[] classBytes) {
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        RemappingVisitor remappingVisitor = new RemappingVisitor(writer, mappingManager, hierarchyManager);
        reader.accept(remappingVisitor, ClassReader.EXPAND_FRAMES);

        return new ClassFile(mappingManager.getClass(name), writer.toByteArray());
    }
}
