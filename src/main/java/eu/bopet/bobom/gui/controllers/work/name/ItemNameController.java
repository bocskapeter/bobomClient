package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.ItemNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class ItemNameController extends NameWorkController {

    public ItemNameController(GUIContext context) {
        super(context.getLabels().getString("nameItem"), ItemNames.class, context);
    }
}
