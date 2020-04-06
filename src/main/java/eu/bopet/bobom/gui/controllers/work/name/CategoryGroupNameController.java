package eu.bopet.bobom.gui.controllers.work.name;

import eu.bopet.bobom.core.entities.names.CategoryGroupNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.work.NameWorkController;

public class CategoryGroupNameController extends NameWorkController {

    public CategoryGroupNameController(GUIContext context) {
        super(context.getLabels().getString("nameCategoryGroup"), CategoryGroupNames.class, context);
    }
}
