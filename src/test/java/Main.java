import dev.tori.runtimeprofiler.Profiler;
import dev.tori.runtimeprofiler.write.OutputWriter;
import org.omnimc.asm.file.IOutputFile;
import org.omnimc.asm.manager.thread.SafeClassManager;
import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.trix.TrixRemapper;
import org.omnimc.trix.contexts.Context;
import org.omnimc.trix.hierarchy.HierarchyManager;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.Deflater;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class Main {

    private static final String MINECRAFT_MAPPINGS = "C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\minecraftmappings.txt";
    private static final String MINECRAFT_JAR = "C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\1.21.jar";
    private static final String OUTPUT_JAR = "C:\\Users\\CryroByte\\Desktop\\TrixRemapper\\modified-1.21-Server.jar";

    /**
     * A OmniMC ModHandler will have things in the META-INF like
     * <p>org.omnimc.handler.IModLoader</p>
     * <p>org.omnimc.handler.IModEntryPoint</p>
     * <p>org.omnimc.handler.mixins.IMixinEntry</p>
     * <p>org.omnimc.handler.trix.access.IAccess</p>
     */


    public static void main(String[] args) throws IOException {

        Profiler profiler = new Profiler("normal");
        profiler.start();

        profiler.push("LuminaReader");
        LuminaReader luminaReader = new LuminaReader();
        ParsingContainer parsingContainer = luminaReader.readPath("C:\\Users\\CryroByte\\Desktop\\Lumina-github\\run");

        profiler.swap("Remapper");
        TrixRemapper trixRemapper = new TrixRemapper(parsingContainer);

        profiler.swap("HierarchyManager");
        HierarchyManager hierarchyManager = new HierarchyManager();

        File minecraft = new File(MINECRAFT_JAR);

        profiler.swap("SafeClassManager Reading");
        SafeClassManager classManager = new SafeClassManager();
        classManager.readJarFile(minecraft);

        profiler.swap("Applying Hierarchy");
        classManager.applyChanges(Context.ofHierarchy(trixRemapper, hierarchyManager));
        hierarchyManager.populateClassFiles();

        profiler.swap("Applying mappings");
        classManager.applyChanges(Context.ofMapping(trixRemapper, hierarchyManager));

        profiler.swap("FileNameChanges");
        classManager.applyChanges(new FileNameChange(trixRemapper));

        profiler.swap("OutputFile");
        IOutputFile output = classManager.outputFile();

        profiler.swap("Output In bytes");
        byte[] fileInBytes = output.getFileInBytes(Deflater.NO_COMPRESSION);

        profiler.swap("Creating file");
        FileOutputStream outputStream = new FileOutputStream(OUTPUT_JAR);
        outputStream.write(fileInBytes);
        outputStream.flush();
        outputStream.close();

        profiler.pop();

        profiler.stop();

        OutputWriter.HTML.writeToPath(profiler, Path.of(System.getProperty("user.dir")));

        // <init> <clinit> <toString> <hashCode>
        // equals
    }
}
