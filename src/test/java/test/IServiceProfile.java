package test;

import org.omnimc.asm.file.IOutputFile;
import test.transformers.ITransformer;

import java.io.File;
import java.util.LinkedHashSet;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IServiceProfile { // A Service Profile contains the namespace and the changes it should do.

    // todo find a way we can get outputs from the the Profile
    // Maybe set a temp file idk
    Namespace getNamespace();

    void getOutputs(IOutputFile... outputFiles);

    File getMappingsFile();

    LinkedHashSet<? extends ITransformer> getTransformers(); // todo find a way to make classchanges then when the hierarchy manager is done populate the manager.

}
