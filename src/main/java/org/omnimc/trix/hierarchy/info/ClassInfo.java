/*
 * MIT License
 *
 * Copyright (c) 2024 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.omnimc.trix.hierarchy.info;

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