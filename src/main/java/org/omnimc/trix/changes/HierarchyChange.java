package org.omnimc.trix.changes;

import org.omnimc.trix.managers.HierarchyManager;
import org.omnimc.trix.visitors.hierarchy.HierarchyVisitor;
import org.omnimc.trix.managers.MappingManager;
import org.objectweb.asm.ClassReader;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyChange implements IClassChange {
    private final MappingManager mappingManager;
    private final HierarchyManager hierarchyManager;

    public HierarchyChange(MappingManager mappingManager, HierarchyManager hierarchyManager) {
        this.mappingManager = mappingManager;
        this.hierarchyManager = hierarchyManager;
    }

    @Override
    public ClassFile applyChange(String name, byte[] classBytes) {
        ClassReader reader = new ClassReader(classBytes);

        HierarchyVisitor hierarchyVisitor = new HierarchyVisitor(mappingManager, hierarchyManager);
        reader.accept(hierarchyVisitor, ClassReader.EXPAND_FRAMES);

        return new ClassFile(name.replace(".class", ""), classBytes);
    }
}
