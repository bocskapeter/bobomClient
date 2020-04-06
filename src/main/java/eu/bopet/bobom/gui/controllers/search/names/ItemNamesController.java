package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.ItemNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class ItemNamesController extends NamesSearchController {

    public ItemNamesController(GUIContext context) {
        super(context.getLabels().getString("namesItem"), ItemNames.class, context);
    }
}
