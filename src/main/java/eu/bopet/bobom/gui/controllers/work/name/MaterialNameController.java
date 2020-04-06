package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.MaterialNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class MaterialNameController extends NameWorkController {

    public MaterialNameController(GUIContext context) {
        super(context.getLabels().getString("nameMaterial"), MaterialNames.class, context);
    }
}
