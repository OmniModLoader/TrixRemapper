package org.omnimc.trix.parser.parsers;

import org.omnimc.trix.file.MappingEntry;
import org.omnimc.trix.managers.MappingManager;
import org.omnimc.trix.parser.IParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class ProguardParser implements IParser {
    protected String currentClassName = null;

    @Override
    public void parse(MappingManager remapper) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(remapper.getCurrentFile()));

            String entry;
            while ((entry = bufferedReader.readLine()) != null) {
                if (entry.isEmpty()) {
                    continue;
                }

                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("#")) {
                    trimmedEntry = trimmedEntry.substring(0, trimmedEntry.indexOf("#"));
                    if (trimmedEntry.isEmpty()) {
                        continue;
                    }
                }

                if (trimmedEntry.charAt(trimmedEntry.length() - 1) == ':') {
                    String currentClassName1 = processClassMapping(trimmedEntry, remapper);
                    if (currentClassName1 == null) {
                        continue;
                    }

                    currentClassName = currentClassName1;
                    continue;
                }

                if (currentClassName != null) {
                    processClassMemberMapping(currentClassName, trimmedEntry, remapper);
                }
            }
        } catch (IOException ignored) {

        }
    }

    private String processClassMapping(String line, MappingManager remapper) {
        // 57:57:
        // number:number:

        int arrowIndex = line.indexOf("->");
        if (arrowIndex < 0) {
            return null;
        }

        int colonIndex = line.indexOf(':', arrowIndex + 2);
        if (colonIndex < 0) {
            return null;
        }

        // Extract the elements.
        String className = line.substring(0, arrowIndex).trim().replace(".", "/");
        String newClassName = line.substring(arrowIndex + 2, colonIndex).trim().replace(".", "/");

        remapper.addClass(newClassName, className);

        return newClassName;
    }


    private void processClassMemberMapping(String className, String line, MappingManager remapper) {
        int colonIndex1 = line.indexOf(':');
        int colonIndex2 = colonIndex1 < 0 ? -1 : line.indexOf(':', colonIndex1 + 1);
        int spaceIndex = line.indexOf(' ', colonIndex2 + 2);
        int argumentIndex1 = line.indexOf('(', spaceIndex + 1);
        int argumentIndex2 = argumentIndex1 < 0 ? -1 : line.indexOf(')', argumentIndex1 + 1);
        int colonIndex3 = argumentIndex2 < 0 ? -1 : line.indexOf(':', argumentIndex2 + 1);
        int colonIndex4 = colonIndex3 < 0 ? -1 : line.indexOf(':', colonIndex3 + 1);
        int arrowIndex = line.indexOf("->", (colonIndex4 >= 0 ? colonIndex4 : colonIndex3 >= 0 ? colonIndex3 : argumentIndex2 >= 0 ? argumentIndex2 : spaceIndex) + 1);

        if (spaceIndex < 0 || arrowIndex < 0) {
            return;
        }

        // Extract the elements.
        String type = line.substring(colonIndex2 + 1, spaceIndex).trim().replace(".", "/");
        String name = line.substring(spaceIndex + 1, argumentIndex1 >= 0 ? argumentIndex1 : arrowIndex).trim();
        String newName = line.substring(arrowIndex + 2).trim();

        // Does the method name contain an explicit original class name?
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex >= 0) {
            className = name.substring(0, dotIndex);

            name = name.substring(dotIndex + 1);
        }

        // Process this class member mapping.
        if (!type.isEmpty() && !name.isEmpty() && !newName.isEmpty()) {
            // Is it a field or a method?
            if (argumentIndex2 < 0) {
                // Field found

                remapper.addField(className, new MappingEntry(newName, name));
            } else {
                String replace = line.substring(argumentIndex1 + 1, argumentIndex2).trim().replace(".", "/");
                String arguments = this.methodFormat(replace);
                String formatedType = this.primitiveTypes(type);

                remapper.addMethod(className, new MappingEntry(newName + arguments + formatedType, name));
            }
        }
    }

    private String methodFormat(String arguments) {
        return "(" + method(arguments) + ")";
    }

    private String method(String arguments) {
        if (arguments.isEmpty()) {
            return arguments;
        }

        String[] split = arguments.split(",");

        StringBuilder returnBuilder = new StringBuilder();
        for (String s : split) {
            returnBuilder.append(primitiveTypes(s));
        }

        return returnBuilder.toString();
    }

    private String primitiveTypes(String input) {
        StringBuilder brackets = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '[') {
                brackets.append("[");
                continue;
            }

            if (c == ']') {
                input = input.replaceFirst("\\[]", "");
            }
        }

        switch (input) { // I am so sorry JitPack wouldnt let me do it the clean way for some reason...
            case "boolean" -> {
                return brackets + "Z";
            }
            case "byte" -> {
                return brackets + "B";
            }
            case "char" -> {
                return brackets + "C";
            }
            case "double" -> {
                return brackets + "D";
            }
            case "float" -> {
                return brackets + "F";
            }
            case "int" -> {
                return brackets + "I";
            }
            case "long" -> {
                return brackets + "J";
            }
            case "void" -> {
                return  "V";
            }
            default -> {
                return brackets + "L" + input + ";";
            }
        }
    }
}
