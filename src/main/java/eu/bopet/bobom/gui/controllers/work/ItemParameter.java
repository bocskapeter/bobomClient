package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.CategoryParameters;
import eu.bopet.bobom.core.entities.ParameterValues;

public class ItemParameter {
    private CategoryParameters categoryParameter;
    private ParameterValues parameterValue;

    public ItemParameter(CategoryParameters categoryParameter, ParameterValues parameterValue) throws Exception {
        this.categoryParameter = categoryParameter;
        this.parameterValue = parameterValue;
        if (!categoryParameter.getParameter().equals(parameterValue.getParameter())) {
            throw new Exception("Category parameter not identical with item parameter!");
        }
    }

    public CategoryParameters getCategoryParameter() {
        return categoryParameter;
    }

    public ParameterValues getParameterValue() {
        return parameterValue;
    }
}
