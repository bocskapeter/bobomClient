package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.ParameterNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class ParameterNameController extends NameWorkController {

    public ParameterNameController(GUIContext context) {
        super(context.getLabels().getString("nameParameter"), ParameterNames.class, context);
    }
}
