package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.MaterialNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class MaterialNamesController extends NamesSearchController {

    public MaterialNamesController(GUIContext context) {
        super(context.getLabels().getString("namesMaterial"), MaterialNames.class, context);
    }
}
