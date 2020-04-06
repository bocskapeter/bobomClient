package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.CategoryNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class CategoryNameController extends NameWorkController {

    public CategoryNameController(GUIContext context) {
        super(context.getLabels().getString("nameCategory"), CategoryNames.class, context);
    }
}
