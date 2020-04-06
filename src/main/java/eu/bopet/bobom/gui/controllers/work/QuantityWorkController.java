package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Quantities;
import eu.bopet.bobom.core.entities.names.QuantityNames;
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

public class QuantityWorkController extends GUIController implements Initializable {

    @FXML
    private TextField quantityName;
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
    private DBEntities currentQuantityName;

    public QuantityWorkController(GUIContext context) {
        super("Quantity", Quantities.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantityName.setDisable(true);
        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Quantities quantity = (Quantities) newValue;
                this.setCurrentEntity(quantity);
                this.currentQuantityName = quantity.getQuantityName();
                quantityName.textProperty().set(quantity.getQuantityName().getNameEn());
                description.textProperty().set(quantity.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Fields are copied.");
            }
        });
        getContext().getSelection(QuantityNames.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                QuantityNames quantityName = (QuantityNames) newValue;
                this.quantityName.textProperty().set(quantityName.getNameEn());
                this.currentQuantityName = quantityName;
            }
        });
        clearButton.setOnAction(event -> {
            this.currentQuantityName = null;
            this.setCurrentEntity(null);
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
                    this.currentQuantityName = null;
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
        quantityName.textProperty().set("");
        description.textProperty().set("");
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText("Fields are cleared.");
    }

    private boolean validate() {
        if (validateTextField(quantityName) ||
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
        return new Object[]{description.getText(), currentQuantityName};
    }
}
