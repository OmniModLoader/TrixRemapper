package org.omnimc.trix.managers;

import org.omnimc.trix.file.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <h6>Manages the hierarchy of class files and provides methods to add and retrieve class information.
 * <p>Also supports method lookup based on class hierarchy.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyManager {
    private final HashMap<String, ClassInfo> classFiles = new HashMap<>();

    /**
     * <h6>Adds a class file to the manager if it doesn't already exist.
     *
     * @param name The name of the class file.
     * @param file The ClassInfo object representing the class file.
     */
    public void addClassFile(String name, ClassInfo file) {
        if (classFiles.containsKey(name)) {
            return;
        }

        classFiles.put(name, file);
    }

    /**
     * <h6>Retrieves the method name based on the owner class, method name, and descriptor.
     *
     * @param owner      The owner class of the method.
     * @param name       The name of the method.
     * @param descriptor The descriptor of the method.
     * @return The method name, or the original name if not found.
     */
    public String getMethodName(String owner, String name, String descriptor) {
        ClassInfo classInfo = classFiles.get(owner);
        if (classInfo == null) {
            return name;
        }


        return classInfo.getMethods().getOrDefault(name + descriptor, null);
    }

    public String getFieldName(String owner, String name, String descriptor) {
        ClassInfo classInfo = classFiles.get(owner);
        if (classInfo == null) {
            return name;
        }


        return classInfo.getFields().getOrDefault(name + descriptor, null);
    }

    /**
     * <h6>Populates the class files with their dependencies resolved.
     * <p>Uses a recursive lookup mechanism to resolve dependencies.
     */
    public void populateClassFiles() {
        HashMap<String, ClassInfo> classFileHashMap = new HashMap<>();

        for (Map.Entry<String, ClassInfo> entry : classFiles.entrySet()) {
            String className = entry.getKey();
            ClassInfo originalClassFile = entry.getValue();

            ClassInfo lookup = this.lookup(originalClassFile, originalClassFile.getDependentClasses());

            classFileHashMap.put(className, lookup);
        }

        classFiles.clear();
        classFiles.putAll(classFileHashMap);
    }

    /**
     * <h6>Recursively looks up dependencies for a given class file and resolves them.
     *
     * @param originalClassFile The original class file to resolve.
     * @param dependentClasses  The list of dependent classes.
     * @return The resolved ClassInfo object.
     */
    private ClassInfo lookup(ClassInfo originalClassFile, ArrayList<String> dependentClasses) {
        ClassInfo tempClassFile = originalClassFile;
        ArrayList<String> listOfPossibleLookups = new ArrayList<>();

        for (String dependentClass : dependentClasses) {
            ClassInfo file = classFiles.get(dependentClass);
            if (file == null) {
                continue;
            }

            listOfPossibleLookups.addAll(file.getDependentClasses());

            tempClassFile.getFields().putAll(file.getFields());
            tempClassFile.getMethods().putAll(file.getMethods());
        }

        if (!listOfPossibleLookups.isEmpty()) {
            tempClassFile = lookup(tempClassFile, listOfPossibleLookups);
        }

        return tempClassFile;
    }
}
