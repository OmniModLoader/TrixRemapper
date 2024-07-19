package org.omnimc.trix;

import org.omnimc.trix.mapping.MappingManager;
import org.objectweb.asm.commons.Remapper;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class TrixRemapper extends Remapper {
    private final MappingManager mappingManager;

    public TrixRemapper(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }


    @Override
    public String map(String internalName) {
        return mapType(internalName);
    }

    @Override
    public String mapType(String internalName) {
        return mappingManager.getClass(internalName);
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        return mappingManager.getMethodName(owner, name, descriptor);
    }

    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        return mappingManager.getFieldName(owner, name);
    }
}
