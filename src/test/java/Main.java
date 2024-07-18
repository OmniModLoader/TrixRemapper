/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class Main {

/*    public static void main(String[] args) throws IOException {

        *//* Setting up managers and parsers *//*

        ProguardParser parser = new ProguardParser();
        MappingManager mappingManager = new MappingManager(new File("C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\minecraftmappings.txt"), parser);
        mappingManager.applyParsings();

        HierarchyManager hierarchyManager = new HierarchyManager();

        TrixRemapper trixRemapper = new TrixRemapper(mappingManager);

        mappingManager.setRemapper(trixRemapper);


        File minecraft = new File("C:\\Users\\CryroByte\\Desktop\\protection\\TrixObfuscator\\1.21.jar");

        SafeClassManager classManager = new SafeClassManager();
        classManager.readJarFile(minecraft);

        Profiler profiler = new Profiler("hierarchy", TimeUnit.NANOSECONDS);
        profiler.start();

        profiler.push("Overall Time");

        profiler.push("HierarchyChange");
        classManager.applyChanges(new HierarchyChange(mappingManager, hierarchyManager));
        hierarchyManager.populateClassFiles();

        profiler.swap("RemappingChange");
        classManager.applyChanges(new RemappingChange(mappingManager, hierarchyManager));
        profiler.pop();

        profiler.pop();

        profiler.stop();

        OutputWriter.HTML.writeToPath(profiler, new File("C:\\Users\\CryroByte\\Desktop\\Universal-Projects\\Trix").toPath());

        IOutputFile outputFile = classManager.outputFile();
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\CryroByte\\Desktop\\Universal-Projects\\Trix\\modified-1.21-Server.jar");
        outputStream.write(outputFile.getFileInBytes(Deflater.DEFLATED));
        outputStream.flush();
        outputStream.close();
        // <init> <clinit> <toString> <hashCode>
        // equals
    }*/
}
