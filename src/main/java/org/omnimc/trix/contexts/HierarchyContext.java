package org.omnimc.trix.contexts;

import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.contexts.interfaces.IHierarchyContext;
import org.omnimc.trix.hierarchy.info.ClassInfo;
import org.omnimc.trix.hierarchy.HierarchyManager;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyContext implements IHierarchyContext {
    private final HierarchyManager hierarchyManager;
    private final Remapper globalRemapper;
    private ClassInfo classInfo;

    public HierarchyContext(HierarchyManager hierarchyManager, Remapper globalRemapper) {
        this.hierarchyManager = hierarchyManager;
        this.globalRemapper = globalRemapper;
    }

    @Override
    public void visit(int access, String name, String superName, String[] interfaces) {
        this.classInfo = new ClassInfo(name);

        classInfo.addDependentClass(superName);

        if (interfaces != null) {
            for (String anInterface : interfaces) {
                classInfo.addDependentClass(anInterface);
            }
        }
    }

    @Override
    public void visitMethod(int access, String name, String descriptor) {
        if (isPrivateAccess(access)) {
            return;
        }

        classInfo.addMethod(name + descriptor, globalRemapper.mapMethodName(classInfo.getClassName(), name, globalRemapper.mapMethodDesc(descriptor)));
    }

    @Override
    public void visitField(int access, String name, String descriptor) {
        if (isPrivateAccess(access)) {
            return;
        }

        classInfo.addField(name + descriptor, globalRemapper.mapFieldName(classInfo.getClassName(), name, descriptor));
    }

    @Override
    public void visitEnd() {
        hierarchyManager.addClassFile(classInfo.getClassName(), classInfo);
    }
}