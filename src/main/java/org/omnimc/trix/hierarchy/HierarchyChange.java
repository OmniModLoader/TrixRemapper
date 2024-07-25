package org.omnimc.trix.hierarchy;

import org.omnimc.trix.contexts.interfaces.IHierarchyContext;
import org.omnimc.trix.visitors.hierarchy.HierarchyVisitor;
import org.objectweb.asm.ClassReader;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyChange implements IClassChange {
    private final IHierarchyContext hierarchyContext;

    public HierarchyChange(IHierarchyContext hierarchyContext) {
        this.hierarchyContext = hierarchyContext;
    }

    @Override
    public ClassFile applyChange(String name, byte[] classBytes) {
        ClassReader reader = new ClassReader(classBytes);

        HierarchyVisitor hierarchyVisitor = new HierarchyVisitor(hierarchyContext);
        reader.accept(hierarchyVisitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);

        return new ClassFile(name.replace(".class", ""), classBytes);
    }
}