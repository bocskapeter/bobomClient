package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Parameters;
import eu.bopet.bobom.core.entities.Units;
import eu.bopet.bobom.core.entities.names.ParameterNames;
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

public class ParameterWorkController extends GUIController implements Initializable {

    @FXML
    private TextField parameterName;
    @FXML
    private TextField parameterUnit;
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
    private DBEntities currentParameterName;
    private DBEntities currentParameterUnit;

    public ParameterWorkController(GUIContext context) {
        super("Parameter", Parameters.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parameterName.setDisable(true);
        parameterUnit.setDisable(true);
        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Parameters parameter = (Parameters) newValue;
                this.setCurrentEntity(parameter);
                this.currentParameterName = parameter.getParameterName();
                this.currentParameterUnit = parameter.getParameterUnit();
                parameterName.textProperty().set(parameter.getParameterName().getNameEn());
                parameterUnit.textProperty().set(parameter.getParameterUnit().getUnitName().getNameEn());
                description.textProperty().set(parameter.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Fields are copied.");
            }
        });
        getContext().getSelection(ParameterNames.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ParameterNames parameterName = (ParameterNames) newValue;
                this.parameterName.textProperty().set(parameterName.getNameEn());
                this.currentParameterName = parameterName;
            }
        });
        getContext().getSelection(Units.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Units unit = (Units) newValue;
                this.parameterUnit.textProperty().set(unit.getUnitName().getNameEn());
                this.currentParameterUnit = unit;
            }
        });
        clearButton.setOnAction(event -> {
            this.currentParameterName = null;
            this.setCurrentEntity(null);
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
                    this.currentParameterName = null;
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
        parameterName.textProperty().set("");
        parameterUnit.textProperty().set("");
        description.textProperty().set("");
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText("Fields are cleared.");
    }

    private boolean validate() {
        if (validateTextField(parameterName) ||
                validateTextField(parameterUnit) ||
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
        return new Object[]{description.getText(), currentParameterName, currentParameterUnit};
    }
}
