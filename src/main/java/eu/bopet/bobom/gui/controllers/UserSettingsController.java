package eu.bopet.bobom.gui.controllers;

import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserSettingsController implements Initializable {

    private final GUIContext context;
    private final MainStageController mainStageController;
    @FXML
    private Label userSettingsLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField eMailTextField;
    @FXML
    private Label userNameLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField oldPasswordField;
    @FXML
    private Label newPasswordLabel;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Label confirmNewPasswordLabel;
    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private Label languageLabel;
    @FXML
    private ChoiceBox<Locale> languageChoiceBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public UserSettingsController(GUIContext context, MainStageController mainStageController) {
        this.context = context;
        this.mainStageController = mainStageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userSettingsLabel.setText(context.getLabels().getString("userSettings"));
        emailLabel.setText(context.getLabels().getString("email") + Utils.REQUIRED_FIELD);
        userNameLabel.setText(context.getLabels().getString("userName") + Utils.REQUIRED_FIELD);
        passwordLabel.setText(context.getLabels().getString("password") + Utils.REQUIRED_FIELD);
        newPasswordLabel.setText(context.getLabels().getString("newPassword") + Utils.REQUIRED_FIELD);
        confirmNewPasswordLabel.setText(context.getLabels().getString("confirmNewPassword") + Utils.REQUIRED_FIELD);
        languageLabel.setText(context.getLabels().getString("language"));

        eMailTextField.textProperty().set(context.getUser().getEMail());
        userNameTextField.textProperty().set(context.getUser().getName());
 /*       saveButton.setOnAction(event -> {
            if (validate()) {
                Users user = context.getDataModel().getById(context.getUser().getId(), Users.class);
                user.setEMail(eMailTextField.getText());
                user.setName(userNameTextField.getText());
                user.setPassword(BCrypt.hashpw(newPasswordField.getText(), BCrypt.gensalt()));
                user.setLocale(languageChoiceBox.getSelectionModel().getSelectedItem());
                if (context.getDataModel().modify(user, Users.class)) {
                    mainStageController.loadEngineeringScene();
                }
            }
        });*/
        cancelButton.setOnAction(event -> mainStageController.loadEngineeringScene());
        languageChoiceBox.getItems().add(Locale.getDefault());
        languageChoiceBox.getItems().add(Locale.GERMANY);
        Locale hungary = new Locale("hu", "HU");
        languageChoiceBox.getItems().add(hungary);
        languageChoiceBox.getSelectionModel().select(context.getUser().getLocale());
    }

    private boolean validate() {
        if (validateTextField(eMailTextField) ||
                validateTextField(userNameTextField) ||
                validateTextField(oldPasswordField) ||
                validateTextField(newPasswordField) ||
                validateTextField(confirmNewPasswordField)) {
            return false;
        }
        if (!BCrypt.checkpw(oldPasswordField.getText(), context.getUser().getPassword())) {
            oldPasswordField.textProperty().set("");
            highlightError(oldPasswordField);
            return false;
        }
        if (!newPasswordField.getText().equals(confirmNewPasswordField.getText()) ||
                oldPasswordField.getText().equals(newPasswordField.getText())) {
            newPasswordField.textProperty().set("");
            confirmNewPasswordField.textProperty().set("");
            highlightError(newPasswordField);
            return false;
        }
        return true;
    }

    private boolean validateTextField(TextField textFieldName) {
        if (textFieldName.getText() != null && textFieldName.getText().isEmpty()) {
            highlightError(textFieldName);
            return true;
        } else return false;
    }

    private void highlightError(TextField textFieldName) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(context.getLabels().getString("fillRequiredFields"));
        textFieldName.requestFocus();
    }
}
