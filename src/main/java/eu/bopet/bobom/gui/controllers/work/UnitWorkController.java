package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Quantities;
import eu.bopet.bobom.core.entities.Units;
import eu.bopet.bobom.core.entities.names.UnitNames;
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

public class UnitWorkController extends GUIController implements Initializable {

    @FXML
    private TextField unitName;
    @FXML
    private TextField unitQuantity;
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
    private DBEntities currentUnitName;
    private DBEntities currentUnitQuantity;

    public UnitWorkController(GUIContext context) {
        super("Unit", Units.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        unitName.setDisable(true);
        unitQuantity.setDisable(true);
        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Units unit = (Units) newValue;
                this.setCurrentEntity(unit);
                this.currentUnitName = unit.getUnitName();
                this.currentUnitQuantity = unit.getUnitQuantity();
                unitName.textProperty().set(unit.getUnitName().getNameEn());
                unitQuantity.textProperty().set(unit.getUnitQuantity().getQuantityName().getNameEn());
                description.textProperty().set(unit.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Fields are copied.");
            }
        });
        getContext().getSelection(UnitNames.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                UnitNames unitName = (UnitNames) newValue;
                this.unitName.textProperty().set(unitName.getNameEn());
                this.currentUnitName = unitName;
            }
        });
        getContext().getSelection(Quantities.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Quantities quantity = (Quantities) newValue;
                this.unitQuantity.textProperty().set(quantity.getQuantityName().getNameEn());
                this.currentUnitQuantity = quantity;
            }
        });
        clearButton.setOnAction(event -> {
            this.currentUnitName = null;
            this.setCurrentEntity(null);
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
                    this.currentUnitName = null;
                    this.setCurrentEntity(null);
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText("Successfully deleted.");
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Error. Object not found or in use!");
                }
            } else {
                clearFields();
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Error. No object selected!");
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
                errorLabel.setText(getDisplayName() + " has been saved.");
            }
        });
        modifyButton.setOnAction(event -> {
            if (validate()) {
                if (getCurrentEntity() != null) {
                    if (getContext().modifyEntity(getCurrentEntity(), getFields())) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getDisplayName() + " has been modified.");
                    } else {
                        errorLabel.setTextFill(Color.RED);
                        errorLabel.setText("Error. Object not found!");
                    }
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Error. No Object selected!");
                }
            }
        });
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
    }

    private void clearFields() {
        unitName.textProperty().set("");
        unitQuantity.textProperty().set("");
        description.textProperty().set("");
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText("Fields are cleared.");
    }

    private boolean validate() {
        if (validateTextField(unitName) ||
                validateTextField(unitQuantity) ||
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
        errorLabel.setText(getDisplayName() + " already exists. Error code: " + id.toString());
    }

    private boolean validateTextField(TextField textFieldName) {
        if (textFieldName.getText() != null && textFieldName.getText().isEmpty()) {
            highlightError(textFieldName);
            return true;
        } else return false;
    }

    private void highlightError(TextField textFieldName) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText("Fill required fields. *");
        textFieldName.requestFocus();
    }

    private Object[] getFields() {
        return new Object[]{description.getText(), currentUnitQuantity, currentUnitName};
    }
}
