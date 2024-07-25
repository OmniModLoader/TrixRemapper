package org.omnimc.trix.contexts;

import org.objectweb.asm.commons.Remapper;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.trix.hierarchy.HierarchyChange;
import org.omnimc.trix.hierarchy.HierarchyManager;
import org.omnimc.trix.mapping.MappingChange;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class Context {

    public static IClassChange ofMapping(Remapper remapper, HierarchyManager hierarchyManager) {
        return new MappingChange(new MappingContext(remapper, hierarchyManager));
    }

    public static IClassChange ofHierarchy(Remapper remapper, HierarchyManager hierarchyManager) {
        return new HierarchyChange(new HierarchyContext(hierarchyManager, remapper));
    }
}