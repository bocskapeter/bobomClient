package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.names.Names;
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

public abstract class NameWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Label labelNameEn;
    @FXML
    private Label labelNameDe;
    @FXML
    private Label labelNameHu;
    @FXML
    private TextField textFieldNameEn;
    @FXML
    private TextField textFieldNameDe;
    @FXML
    private TextField textFieldNameHu;
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

    public NameWorkController(String name, Class<?> namesClass, GUIContext context) {
        super(name, namesClass, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getDisplayName());
        secondTitleLabel.setText(getContext().getLabels().getString("createNewModifyOrDelete"));
        labelNameEn.setText(getDisplayName() + " - " + getContext().getLabels().getString("english") + " *");
        labelNameDe.setText(getDisplayName() + " - " + getContext().getLabels().getString("german") + " *");
        labelNameHu.setText(getDisplayName() + " - " + getContext().getLabels().getString("hungarian") + " *");
        clearButton.setText(getContext().getLabels().getString("clear"));
        deleteButton.setText(getContext().getLabels().getString("delete"));
        modifyButton.setText(getContext().getLabels().getString("modify"));
        saveButton.setText(getContext().getLabels().getString("save"));
        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Names names = (Names) newValue;
                this.setCurrentEntity(names);
                textFieldNameEn.textProperty().set(names.getNameEn());
                textFieldNameDe.textProperty().set(names.getNameDe());
                textFieldNameHu.textProperty().set(names.getNameHu());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
            }
        });
        clearButton.setOnAction(event -> {
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
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
        modifyButton.setOnAction(event -> {
            if (validate()) {
                Object[] fields = {textFieldNameEn.getText(), textFieldNameDe.getText(), textFieldNameHu.getText()};
                if (getCurrentEntity() != null) {
                    if (getContext().modifyEntity(getCurrentEntity(), fields)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getDisplayName() + getContext().getLabels().getString("hasBeenModified"));
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
        saveButton.setOnAction(event -> {
            if (validate()) {
                String[] fields = {
                        textFieldNameEn.getText(),
                        textFieldNameDe.getText(),
                        textFieldNameHu.getText()};
                DBEntities entity = getContext().saveNewEntity(fields, getEntityClass());
                if (entity != null) {
                    setCurrentEntity(null);
                }
                clearFields();
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getDisplayName() + getContext().getLabels().getString("hasBeenModified"));
            }
        });
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
    }

    private void clearFields() {
        textFieldNameEn.textProperty().set("");
        textFieldNameDe.textProperty().set("");
        textFieldNameHu.textProperty().set("");

    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
    }

    private boolean validate() {
        if (validateTextField(textFieldNameEn) ||
                validateTextField(textFieldNameDe) ||
                validateTextField(textFieldNameHu)) {
            return false;
        } else {
            return unique();
        }
    }

    private boolean unique() {
        Object[] fields = {
                textFieldNameEn.getText(),
                textFieldNameDe.getText(),
                textFieldNameHu.getText()};
        //TODO
        //UUID nameId = getContext().getDataModel().getId(fields, getEntityClass());
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
        errorLabel.setText(getDisplayName() + getContext().getLabels().getString("alreadyExists") + " "
                + getContext().getLabels().getString("errorCode") + id.toString());
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
}
