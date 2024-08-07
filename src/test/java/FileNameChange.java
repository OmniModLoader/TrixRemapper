import org.objectweb.asm.commons.Remapper;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class FileNameChange implements IClassChange {
    private final Remapper remapper;

    public FileNameChange(Remapper remapper) {
        this.remapper = remapper;
    }

    @Override
    public ClassFile applyChange(String name, byte[] classBytes) {
        return new ClassFile(remapper.mapType(name), classBytes);
    }
}