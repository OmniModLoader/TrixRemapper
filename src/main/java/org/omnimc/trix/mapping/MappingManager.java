package org.omnimc.trix.mapping;

import org.omnimc.trix.mapping.entry.MappingEntry;
import org.omnimc.trix.mapping.parser.IParser;
import org.objectweb.asm.commons.Remapper;

import java.io.File;
import java.util.HashMap;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingManager {
    private final File currentFile;
    private final HashMap<String, String> classInformation = new HashMap<>();
    private final HashMap<String, HashMap<String, String>> fieldInformation = new HashMap<>();
    private final HashMap<String, HashMap<String, String>> methodInformation = new HashMap<>();

    private final IParser parser;

    private Remapper remapper;

    public MappingManager(File currentFile, IParser parser) {
        this.currentFile = currentFile;
        this.parser = parser;
    }

    public void applyParsings() {
        parser.parse(this);
    }

    public void addClass(String obfuscatedName, String originalName) {
        if (classInformation.containsKey(obfuscatedName)) {
            return;
        }

        classInformation.put(obfuscatedName, originalName);
    }

    public String getClass(String obfuscatedName) {
        return classInformation.getOrDefault(obfuscatedName, obfuscatedName);
    }

    public void addMethod(String owner, MappingEntry entry) {
        HashMap<String, String> tempHash = methodInformation.get(owner);

        if (tempHash == null) {
            tempHash = new HashMap<>();
        }

        tempHash.put(entry.obfuscatedName(), entry.originalName());

        methodInformation.put(owner, tempHash);
    }

    public String getMethodName(String owner, String name, String descriptor) {
        HashMap<String, String> tempHash = methodInformation.get(owner);
        if (tempHash == null) {
            return name;
        }

        return tempHash.getOrDefault(name + descriptor, name);
    }

    public void addField(String owner, MappingEntry entry) {
        HashMap<String, String> tempHash = fieldInformation.get(owner);

        if (tempHash == null) {
            tempHash = new HashMap<>();
        }

        tempHash.put(entry.obfuscatedName(), entry.originalName());

        fieldInformation.put(owner, tempHash);
    }

    public String getFieldName(String owner, String name) {
        HashMap<String, String> tempHash = fieldInformation.get(owner);
        if (tempHash == null) {
            return name;
        }

        return tempHash.getOrDefault(name, name);
    }

    public void setRemapper(Remapper remapper) {
        if (this.remapper == null) {
            this.remapper = remapper;
        }
    }

    public Remapper getRemapper() {
        return this.remapper;
    }

    public File getCurrentFile() {
        return currentFile;
    }
}
