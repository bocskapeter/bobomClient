package eu.bopet.bobom.gui.controllers.work;


import eu.bopet.bobom.core.entities.CategoryGroups;
import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.names.CategoryGroupNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class CategoryGroupWorkController extends GUIController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private TextField categoryGroupName;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField description;
    @FXML
    private Button clearButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label errorLabel;
    private DBEntities currentCategoryGroupName;

    public CategoryGroupWorkController(GUIContext context) {
        super(context.getLabels().getString("categoryGroup"), CategoryGroups.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameLabel.setText(getContext().getLabels().getString("name"));
        descriptionLabel.setText(getContext().getLabels().getString("description"));

        categoryGroupName.setDisable(true);
        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CategoryGroups categoryGroups = (CategoryGroups) newValue;
                this.setCurrentEntity(categoryGroups);
                this.currentCategoryGroupName = categoryGroups.getCategoryGroupName();
                categoryGroupName.textProperty().set(categoryGroups.getCategoryGroupName().getNameEn());
                description.textProperty().set(categoryGroups.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
            }
        });
        getContext().getSelection(CategoryGroupNames.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CategoryGroupNames categoryGroupNames = (CategoryGroupNames) newValue;
                this.categoryGroupName.textProperty().set(categoryGroupNames.getNameEn());
                this.currentCategoryGroupName = categoryGroupNames;
            }
        });
        clearButton.setOnAction(event -> {
            this.currentCategoryGroupName = null;
            this.setCurrentEntity(null);
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
                    this.currentCategoryGroupName = null;
                    this.setCurrentEntity(null);
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("errorObjectNotFoundOrInUse"));
                }
            } else {
                clearFields();
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("errorNoObjectSelected"));
            }
        });
        saveButton.setOnAction(event -> {
            if (validate()) {
                DBEntities entity = getContext().saveNewEntity(getFields(), getEntityClass());
                if (entity != null) {
                    setCurrentEntity(null);
                }
                clearFields();
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getDisplayName() + " " + getContext().getLabels().getString("hasBeenSaved"));
            }
        });
        modifyButton.setOnAction(event -> {
            if (validate()) {
                if (getCurrentEntity() != null) {
                    if (getContext().modifyEntity(getCurrentEntity(), getFields())) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getDisplayName() + " " + getContext().getLabels().getString("hasBeenModified"));
                    } else {
                        errorLabel.setTextFill(Color.RED);
                        errorLabel.setText(getContext().getLabels().getString("errorObjectNotFound"));
                    }
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("errorNoObjectSelected"));
                }
            }
        });
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
    }

    private void clearFields() {
        categoryGroupName.textProperty().set("");
        description.textProperty().set("");
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
    }

    private boolean validate() {
        if (validateTextField(categoryGroupName) ||
                validateTextField(description)) {
            return false;
        } else {
            return unique();
        }
    }

    private boolean unique() {
        //TODO
        //UUID nameId = getContext().getDataModel().getId(getFields(), getEntityClass());
        UUID nameId = null;
        if (nameId != null) {
            notUniqueError(nameId);
            return false;
        } else {
            return true;
        }
    }

    private void notUniqueError(UUID id) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(getDisplayName() + " " + getContext().getLabels().getString("alreadyExists") + " " + getContext().getLabels().getString("errorCode") + " " + id.toString());
    }

    private boolean validateTextField(TextField textFieldName) {
        if (textFieldName.getText() != null && textFieldName.getText().isEmpty()) {
            highlightError(textFieldName);
            return true;
        } else return false;
    }

    private void highlightError(TextField textFieldName) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
        textFieldName.requestFocus();
    }

    private Object[] getFields() {
        return new Object[]{description.getText(), currentCategoryGroupName};
    }
}
