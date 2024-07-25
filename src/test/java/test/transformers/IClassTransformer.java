package test.transformers;

import org.omnimc.asm.changes.IClassChange;
import test.ParameterType;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IClassTransformer extends ITransformer {

    IClassChange transform(ParameterType... types);

}
