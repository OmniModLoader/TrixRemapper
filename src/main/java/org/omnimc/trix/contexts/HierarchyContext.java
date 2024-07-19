package org.omnimc.trix.contexts;

import org.omnimc.trix.contexts.interfaces.IHierarchyContext;
import org.omnimc.trix.hierarchy.info.ClassInfo;
import org.omnimc.trix.hierarchy.HierarchyManager;
import org.omnimc.trix.mapping.MappingManager;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyContext implements IHierarchyContext {
    private final HierarchyManager hierarchyManager;
    private final MappingManager mappingManager;
    private ClassInfo classInfo;

    public HierarchyContext(HierarchyManager hierarchyManager, MappingManager mappingManager) {
        this.hierarchyManager = hierarchyManager;
        this.mappingManager = mappingManager;
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

        classInfo.addMethod(name + descriptor, mappingManager.getMethodName(classInfo.getClassName(), name, mappingManager.getRemapper().mapMethodDesc(descriptor)));
    }

    @Override
    public void visitField(int access, String name, String descriptor) {
        if (isPrivateAccess(access)) {
            return;
        }

        classInfo.addField(name + descriptor, mappingManager.getFieldName(classInfo.getClassName(), name));
    }

    @Override
    public void visitEnd() {
        hierarchyManager.addClassFile(classInfo.getClassName(), classInfo);
    }
}
