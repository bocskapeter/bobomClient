package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.UnitNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class UnitNamesController extends NamesSearchController {

    public UnitNamesController(GUIContext context) {
        super(context.getLabels().getString("namesUnit"), UnitNames.class, context);
    }
}
