package test.transformers;

import org.omnimc.asm.changes.IResourceChange;
import test.ParameterType;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IResourceTransformer extends ITransformer {

    IResourceChange transform(ParameterType... types);

}
