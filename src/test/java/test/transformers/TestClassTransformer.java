package test.transformers;

import org.omnimc.asm.changes.IClassChange;
import test.ParameterType;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since
 */
public class TestClassTransformer implements IClassTransformer {

    @Override
    public IClassChange transform(ParameterType... types) {
/*        MappingManager mappingManager = null;
        HierarchyManager hierarchyManager = null;

        for (ParameterType type : types) {
            if (mappingManager != null && hierarchyManager != null) {
                break;
            }

            if (type.getType() instanceof MappingManager) {
                mappingManager = (MappingManager) type.getType();
            } else if (type.getType() instanceof HierarchyManager) {
                hierarchyManager = (HierarchyManager) type.getType();
            }
        }

        assert mappingManager != null;
        return new MappingChange(new MappingContext(mappingManager, hierarchyManager));*/
        return null;
    }
}
