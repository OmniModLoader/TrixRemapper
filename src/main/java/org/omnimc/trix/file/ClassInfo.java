package org.omnimc.trix.file;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class ClassInfo {
    private final String className;

    private final ArrayList<String> dependentClasses;

    private final HashMap<String, String> fields;
    private final HashMap<String, String> methods;

    public ClassInfo(String className) {
        this.className = className;

        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
        this.dependentClasses = new ArrayList<>();
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<String> getDependentClasses() {
        return dependentClasses;
    }

    public void addDependentClass(String className) {
        if (dependentClasses.contains(className)) {
            return;
        }

        dependentClasses.add(className);
    }

    public HashMap<String, String> getMethods() {
        return methods;
    }

    public void addMethod(String name, String addition) {
        if (methods.containsKey(name)) {
            return;
        }

        methods.put(name, addition);
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    public void addField(String name, String addition) {
        if (fields.containsKey(name)) {
            return;
        }

        fields.put(name, addition);
    }
}
