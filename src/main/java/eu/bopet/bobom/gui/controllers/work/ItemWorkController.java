package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.names.ItemNames;
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

public class ItemWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label numberLabel;
    @FXML
    private TextField itemNumber;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField itemName;
    @FXML
    private Label revisionLabel;
    @FXML
    private TextField itemRevision;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField itemStatus;
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
    private DBEntities currentItemName;

    public ItemWorkController(GUIContext context) {
        super(context.getLabels().getString("item"), Items.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("item"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewModifyOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));
        deleteButton.setText(getContext().getLabels().getString("delete"));
        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        revisionLabel.setText(getContext().getLabels().getString("revision") + Utils.REQUIRED_FIELD);
        statusLabel.setText(getContext().getLabels().getString("status") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);
        modifyButton.setText(getContext().getLabels().getString("modify"));
        saveButton.setText(getContext().getLabels().getString("save"));

        itemName.setDisable(true);
        itemRevision.setDisable(true);
        itemStatus.setDisable(true);

        itemRevision.textProperty().set("1");
        itemStatus.textProperty().set("1");

        getContext().getSelection(getEntityClass()).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Items item = (Items) newValue;
                this.setCurrentEntity(item);
                this.currentItemName = item.getItemName();
                itemNumber.textProperty().set(item.getItemNumber());
                itemName.textProperty().set(item.getItemName().getName(getContext().getLanguage()));
                itemRevision.textProperty().set(String.valueOf(item.getRevision()));
                itemStatus.textProperty().set(String.valueOf(item.getStatus()));
                description.textProperty().set(item.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
            }
        });
        getContext().getSelection(ItemNames.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ItemNames itemName = (ItemNames) newValue;
                this.itemName.textProperty().set(itemName.getName(getContext().getLanguage()));
                this.currentItemName = itemName;
            }
        });
        clearButton.setOnAction(event -> {
            this.currentItemName = null;
            this.setCurrentEntity(null);
            clearFields();
            clearFieldsLabel();
        });
        deleteButton.setOnAction(event -> {
            if (getCurrentEntity() != null) {
                if (getContext().deleteEntity(getCurrentEntity())) {
                    this.currentItemName = null;
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
        itemNumber.textProperty().set("");
        itemName.textProperty().set("");
        itemRevision.textProperty().set("1");
        itemStatus.textProperty().set("1");
        description.textProperty().set("");
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
    }

    private boolean validate() {
        if (validateTextField(itemNumber) ||
                validateTextField(itemName) ||
                validateTextField(itemRevision) ||
                validateTextField(itemStatus) ||
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
        errorLabel.setText(getDisplayName() + " " + getContext().getLabels().getString("alreadyExists") + " " + getContext().getLabels().getString("errorCode") + id.toString());
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
        return new Object[]{itemNumber.getText(), Integer.valueOf(itemRevision.getText()), Integer.valueOf(itemStatus.getText()), description.getText(), currentItemName};
    }
}
