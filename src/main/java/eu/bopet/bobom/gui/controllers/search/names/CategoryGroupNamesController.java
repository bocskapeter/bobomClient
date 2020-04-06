package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.CategoryGroupNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class CategoryGroupNamesController extends NamesSearchController {

    public CategoryGroupNamesController(GUIContext context) {
        super(context.getLabels().getString("namesCategoryGroup"), CategoryGroupNames.class, context);
    }
}
