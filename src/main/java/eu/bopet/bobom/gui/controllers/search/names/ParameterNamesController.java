package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.ParameterNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class ParameterNamesController extends NamesSearchController {

    public ParameterNamesController(GUIContext context) {
        super(context.getLabels().getString("namesParameter"), ParameterNames.class, context);
    }
}
