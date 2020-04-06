package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.StandardNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class StandardNameController extends NameWorkController {

    public StandardNameController(GUIContext context) {
        super(context.getLabels().getString("nameStandard"), StandardNames.class, context);
    }
}
