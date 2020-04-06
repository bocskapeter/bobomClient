package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.QuantityNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class QuantityNameController extends NameWorkController {

    public QuantityNameController(GUIContext context) {
        super(context.getLabels().getString("nameQuantity"), QuantityNames.class, context);
    }
}
