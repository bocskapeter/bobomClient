package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Materials;
import eu.bopet.bobom.core.entities.names.MaterialNames;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
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

public class MaterialWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField materialName;
    @FXML
    private Label numberLabel;
    @FXML
    private TextField materialNumber;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField description;
    @FXML
    private Label errorLabel;
    @FXML
    private Button modifyButton;
    @FXML
    private Button saveButton;
    private MaterialNames currentMaterialName;

    public MaterialWorkController(GUIContext context) {
        super(context.getLabels().getString("material"), Materials.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description"));
        clearButton.setText(getContext().getLabels().getString("clear"));
        deleteButton.setText(getContext().getLabels().getString("delete"));
        modifyButton.setText(getContext().getLabels().getString("modify"));
        saveButton.setText(getContext().getLabels().getString("save"));

        materialName.setDisable(true);
        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Materials material = (Materials) newValue;
                this.setCurrentEntity(material);
                this.currentMaterialName = material.getMaterialName();
                materialName.textProperty().set(material.getMaterialName().getName(getContext().getLanguage()));
                materialNumber.textProperty().set(material.getMaterialNumber());
                description.textProperty().set(material.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
            }
        });
        getContext().getSelection(MaterialNames.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                MaterialNames materialName = (MaterialNames) newValue;
                this.materialName.textProperty().set(materialName.getNameEn());
                this.currentMaterialName = materialName;
            }
        });
        clearButton.setOnAction(event -> {
            this.currentMaterialName = null;
            this.setCurrentEntity(null);
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
                    this.currentMaterialName = null;
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
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getDisplayName() + " " + getContext().getLabels().getString("hasBeenSaved"));
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("errorObjectNotFoundOrInUse"));
                }

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
        materialName.textProperty().set("");
        description.textProperty().set("");
        materialNumber.textProperty().set("");
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
    }

    private boolean validate() {
        if (validateTextField(materialName) ||
                validateTextField(materialNumber)) {
            return false;
        }
        return unique();
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
        errorLabel.setText(getDisplayName() + " " + getContext().getLabels().getString("alreadyExists") +
                " " + getContext().getLabels().getString("errorCode") + id.toString());
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
        return new Object[]{description.getText(), currentMaterialName, materialNumber.getText()};
    }
}
