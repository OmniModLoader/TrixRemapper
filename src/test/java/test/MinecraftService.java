package test;

import org.jetbrains.annotations.NotNull;
import org.omnimc.asm.manager.thread.SafeClassManager;
import org.omnimc.trix.TrixRemapper;
import org.omnimc.trix.hierarchy.HierarchyManager;
import test.transformers.IClassTransformer;
import test.transformers.IResourceTransformer;
import test.transformers.ITransformer;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MinecraftService implements IService {
    private Set<ParameterType> returnTypes = new HashSet<>();


    @Override
    public void init(@NotNull IServiceProfile currentProfile) {
/*        returnTypes.add(() -> {
            ProguardParser parser = new ProguardParser();
            MappingManager mappingManager = new MappingManager(currentProfile.getMappingsFile(), parser);
            mappingManager.applyParsings();

            mappingManager.setRemapper(new TrixRemapper(mappingManager));

            return mappingManager;
        });*/

        returnTypes.add(HierarchyManager::new);

        List<Object> listPossibleFiles = currentProfile.getNamespace().getValues()
                .stream().filter(obj -> obj instanceof File).toList();


        for (Object file : listPossibleFiles) {
            if (file instanceof File) {
                SafeClassManager classManager = new SafeClassManager();
                classManager.readJarFile((File) file);

                for (ITransformer transformer : currentProfile.getTransformers()) {
                    if (transformer instanceof IClassTransformer) {
                        classManager.applyChanges(((IClassTransformer) transformer).transform(returnTypes.toArray(new ParameterType[0])));
                    } else if (transformer instanceof IResourceTransformer) {
                        classManager.applyChanges(((IResourceTransformer) transformer).transform(returnTypes.toArray(new ParameterType[0])));
                    }
                }

                currentProfile.getOutputs(classManager.outputFile());
            }
        }


    }
}
