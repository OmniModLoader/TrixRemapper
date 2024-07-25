package org.omnimc.trix;

import org.omnimc.lumina.paser.ParsingContainer;
import org.objectweb.asm.commons.Remapper;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class TrixRemapper extends Remapper {
    private final ParsingContainer container;

    public TrixRemapper(ParsingContainer container) {
        this.container = container;
    }


    @Override
    public String map(String internalName) {
        return mapType(internalName);
    }

    @Override
    public String mapType(String internalName) {
        return container.getClassName(internalName);
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        return container.getMethodName(owner, name, descriptor);
    }

    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        return container.getFieldName(owner, name);
    }
}