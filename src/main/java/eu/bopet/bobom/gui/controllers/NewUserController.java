package eu.bopet.bobom.gui.controllers;

import eu.bopet.bobom.core.entities.Users;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewUserController implements Initializable {

    private final GUIContext context;
    private final MainStageController mainStageController;
    @FXML
    private Label newUserLabel;
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
    private PasswordField passwordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public NewUserController(GUIContext context, MainStageController mainStageController) {
        this.context = context;
        this.mainStageController = mainStageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newUserLabel.setText(context.getLabels().getString("newUserLabel"));
        emailLabel.setText(context.getLabels().getString("email") + Utils.REQUIRED_FIELD);
        userNameLabel.setText(context.getLabels().getString("userName") + Utils.REQUIRED_FIELD);
        passwordLabel.setText(context.getLabels().getString("password") + Utils.REQUIRED_FIELD);
        confirmPasswordLabel.setText(context.getLabels().getString("confirmPassword") + Utils.REQUIRED_FIELD);
        saveButton.setText(context.getLabels().getString("save"));
        cancelButton.setText(context.getLabels().getString("cancel"));

        saveButton.setOnAction(event -> {
            if (validate()) {
                Users user = new Users();
                user.setEMail(eMailTextField.getText());
                user.setName(userNameTextField.getText());
                user.setPassword(BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt()));
                user.setLocale(Locale.getDefault());
                if (context.saveNew(user) != null) {
                    mainStageController.loadEngineeringScene();
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(context.getLabels().getString("errorSavingNewUser"));
                }
            }
        });
        cancelButton.setOnAction(event -> mainStageController.loadEngineeringScene());
    }

    private void clearFields() {
        eMailTextField.textProperty().set("");
        passwordField.textProperty().set("");
    }

    private boolean validate() {
        if (validateTextField(eMailTextField) ||
                validateTextField(userNameTextField) ||
                validateTextField(passwordField) ||
                validateTextField(confirmPasswordField)) {
            return false;
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            passwordField.textProperty().set("");
            confirmPasswordField.textProperty().set("");
            highlightError(passwordField);
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
