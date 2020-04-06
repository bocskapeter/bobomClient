package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.CategoryNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class CategoryNamesController extends NamesSearchController {

    public CategoryNamesController(GUIContext context) {
        super(context.getLabels().getString("namesCategory"), CategoryNames.class, context);
    }
}
