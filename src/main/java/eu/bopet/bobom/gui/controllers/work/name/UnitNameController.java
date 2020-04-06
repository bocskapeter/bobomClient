package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.UnitNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class UnitNameController extends NameWorkController {

    public UnitNameController(GUIContext context) {
        super(context.getLabels().getString("nameUnit"), UnitNames.class, context);
    }
}
