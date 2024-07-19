import dev.tori.runtimeprofiler.Profiler;
import dev.tori.runtimeprofiler.write.OutputWriter;
import org.omnimc.asm.file.IOutputFile;
import org.omnimc.asm.manager.thread.SafeClassManager;
import org.omnimc.trix.TrixRemapper;
import org.omnimc.trix.contexts.HierarchyContext;
import org.omnimc.trix.contexts.MappingContext;
import org.omnimc.trix.hierarchy.HierarchyChange;
import org.omnimc.trix.mapping.MappingChange;
import org.omnimc.trix.hierarchy.HierarchyManager;
import org.omnimc.trix.mapping.MappingManager;
import org.omnimc.trix.mapping.parser.parsers.ProguardParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;
/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class Main {

    public static void main(String[] args) throws IOException {

        /* Setting up managers and parsers */

        ProguardParser parser = new ProguardParser();
        MappingManager mappingManager = new MappingManager(new File("C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\minecraftmappings.txt"), parser);
        mappingManager.applyParsings();

        TrixRemapper trixRemapper = new TrixRemapper(mappingManager);
        mappingManager.setRemapper(trixRemapper);

        HierarchyManager hierarchyManager = new HierarchyManager();

        File minecraft = new File("C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\1.21.jar");

        SafeClassManager classManager = new SafeClassManager();
        classManager.readJarFile(minecraft);

        Profiler profiler = new Profiler("hierarchy", TimeUnit.NANOSECONDS);
        profiler.start();

        profiler.push("Overall Time");

        profiler.push("HierarchyChange");
        classManager.applyChanges(new HierarchyChange(new HierarchyContext(hierarchyManager, mappingManager)));
        hierarchyManager.populateClassFiles();

        profiler.swap("RemappingChange");
        classManager.applyChanges(new MappingChange(new MappingContext(mappingManager, hierarchyManager)));
        profiler.pop();

        profiler.pop();

        profiler.stop();

        OutputWriter.HTML.writeToPath(profiler, new File("C:\\Users\\CryroByte\\Desktop\\TrixRemapper").toPath());

        IOutputFile outputFile = classManager.outputFile();
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\CryroByte\\Desktop\\TrixRemapper\\modified-1.21-Server.jar");
        outputStream.write(outputFile.getFileInBytes(Deflater.DEFLATED));
        outputStream.flush();
        outputStream.close();
        // <init> <clinit> <toString> <hashCode>
        // equals
    }
}
