package org.omnimc.trix.hierarchy;

import org.objectweb.asm.ClassWriter;
import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.trix.visitors.hierarchy.HierarchyClassVisitor;
import org.objectweb.asm.ClassReader;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyChange implements IClassChange {
    private final HierarchyManager hierarchyManager;
    private final ParsingContainer parsingContainer;

    public HierarchyChange(HierarchyManager hierarchyManager, ParsingContainer parsingContainer) {
        this.hierarchyManager = hierarchyManager;
        this.parsingContainer = parsingContainer;
    }

    @Override
    public ClassFile applyChange(String name, byte[] classBytes) {
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        HierarchyClassVisitor hierarchyVisitor = new HierarchyClassVisitor(writer, hierarchyManager, parsingContainer);
        reader.accept(hierarchyVisitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);

        return new ClassFile(name.replace(".class", ""), classBytes);
    }
}