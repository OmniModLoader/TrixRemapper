package test;

import org.jetbrains.annotations.NotNull;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IService {

    default void init(@NotNull IServiceProfile currentProfile) {

    }

}
