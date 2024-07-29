package test;

import org.omnimc.asm.file.IOutputFile;
import test.transformers.ITransformer;
import test.transformers.TestClassTransformer;

import java.io.File;
import java.util.LinkedHashSet;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since
 */
public class TestClass implements IServiceProfile {

    public static void main(String[] args) {
        MinecraftService service = new MinecraftService();
        TestClass currentProfile = new TestClass();
        service.init(currentProfile);
    }

    @Override
    public Namespace getNamespace() {
/*        File[] files = {new File("C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\1.21.jar")};
        return Namespace.of("omnimc.minecraft", (Object[]) files);*/
        return null;
    }

    @Override
    public void getOutputs(IOutputFile... outputFiles) {
        for (IOutputFile outputFile : outputFiles) {
            System.out.println(outputFile.getFileName());
        }
    }

    @Override
    public File getMappingsFile() {
        return new File("C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\minecraftmappings.txt");
    }


    @Override
    public LinkedHashSet<ITransformer> getTransformers() {
        return null;
    }
}
