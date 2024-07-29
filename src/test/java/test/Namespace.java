package test;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface Namespace { // A namespace contains all the jars needing remapping.

    String getName();

    LinkedHashSet<Object> getValues();

/*
    static Namespace of(@NotNull String name, @NotNull Object... values) {
        return new Namespace() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public LinkedHashSet<Object> getValues() {
                LinkedHashSet<Object> objects = LinkedHashSet.newLinkedHashSet(values.length);
                objects.addAll(List.of(values));

                return objects;
            }

            @Override
            public int hashCode() {
                return getValues().size() + getName().hashCode();
            }
        };
    }
*/

}
