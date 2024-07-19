package org.omnimc.trix.mapping.entry;

import java.util.Objects;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public final class MappingEntry {
    private final String obfuscatedName;
    private final String originalName;

    public MappingEntry(String obfuscatedName, String originalName) {
        this.obfuscatedName = obfuscatedName;
        this.originalName = originalName;
    }

    public String obfuscatedName() {
        return obfuscatedName;
    }

    public String originalName() {
        return originalName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MappingEntry) obj;
        return Objects.equals(this.obfuscatedName, that.obfuscatedName) &&
                Objects.equals(this.originalName, that.originalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obfuscatedName, originalName);
    }

    @Override
    public String toString() {
        return "MappingEntry[" +
                "obfuscatedName=" + obfuscatedName + ", " +
                "originalName=" + originalName + ']';
    }

}
