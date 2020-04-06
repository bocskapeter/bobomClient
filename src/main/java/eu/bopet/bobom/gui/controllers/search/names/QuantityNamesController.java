package eu.bopet.bobom.gui.controllers.search.names;

import eu.bopet.bobom.core.entities.names.QuantityNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.NamesSearchController;

/**
 * @author bocskapeter
 */
public class QuantityNamesController extends NamesSearchController {

    public QuantityNamesController(GUIContext context) {
        super(context.getLabels().getString("namesQuantity"), QuantityNames.class, context);
    }
}
