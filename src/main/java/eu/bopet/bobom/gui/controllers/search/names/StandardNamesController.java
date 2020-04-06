package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.StandardNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class StandardNamesController extends NamesSearchController {

    public StandardNamesController(GUIContext context) {
        super(context.getLabels().getString("namesStandard"), StandardNames.class, context);
    }
}
