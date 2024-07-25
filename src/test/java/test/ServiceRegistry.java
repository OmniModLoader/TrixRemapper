package test;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public final class ServiceRegistry { // This will contain all Namespaces, this is important because a Namespace contains the list of jars needing changes.
    private static final HashMap<Namespace, LinkedHashSet<Object>> registry = new HashMap<>();

    public static LinkedHashSet<Object> get(@NotNull Namespace name) {
        if (registry.isEmpty()) {
            return null;
        }

        return registry.get(name);
    }

    public static LinkedHashSet<Object> register(@NotNull Namespace name) {
        if (registry.containsKey(name)) {
            return registry.get(name);
        }

        return registry.put(name, name.getValues());
    }

}
