package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.entities.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final Stage stage;
    private ResourceBundle labels;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField eMailTextField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private ChoiceBox<Locale> languageChoiceBox;
    @FXML
    private Button loginButton;

    private GUIContext context;

    public LoginController(Stage stage, ResourceBundle labels) {
        this.stage = stage;
        this.labels = labels;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        languageChoiceBox.getItems().add(Locale.getDefault());
        languageChoiceBox.getItems().add(Locale.GERMANY);
        Locale hungary = new Locale("hu", "HU");
        languageChoiceBox.getItems().add(hungary);
        languageChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            labels = ResourceBundle.getBundle("text/LabelsBundle", newValue, new UTF8Control());
            emailLabel.setText(labels.getString("email") + Utils.REQUIRED_FIELD);
            passwordLabel.setText(labels.getString("password") + Utils.REQUIRED_FIELD);
            loginButton.setText(labels.getString("login"));
        });
        languageChoiceBox.getSelectionModel().select(Locale.getDefault());
        emailLabel.setText(labels.getString("email") + Utils.REQUIRED_FIELD);
        passwordLabel.setText(labels.getString("password") + Utils.REQUIRED_FIELD);
        loginButton.setText(labels.getString("login"));
        eMailTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                fireLogin();
            }
        });
        loginButton.setOnKeyPressed(event -> fireLogin());
        loginButton.setOnAction(event -> fireLogin());
    }

    private void fireLogin() {
        if (validate()) {
            attemptLoginMessage();
            try {
                this.context = new GUIContext(labels,eMailTextField.getText());
            } catch (Exception e) {
                errorLabel.setTextFill(Color.RED);
                Tooltip tooltip = new Tooltip(e.getLocalizedMessage());
                errorLabel.setTooltip(tooltip);
                errorLabel.setText(labels.getString("errorDataBaseConnection"));
            }

            Users user = this.context.getUser();
            if (user == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(labels.getString("errorUnknownUser"));
                clearFields();
            } else {
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(labels.getString("checking"));
                if (BCrypt.checkpw(passwordField.getText(), user.getPassword())) {
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(labels.getString("waitLoadingApp"));
                    clearFields();
                    showApp(stage, user);
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(labels.getString("errorWrongPassword"));
                    passwordField.textProperty().set("");
                    passwordField.requestFocus();
                }
            }
        }
    }

    private void showApp(Stage stage, Users user) {
        try {
            System.out.println("jooo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        eMailTextField.textProperty().set("");
        passwordField.textProperty().set("");
    }

    private void attemptLoginMessage() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(labels.getString("loginInProgress"));
    }

    private boolean validate() {
        return textFieldIsValid(eMailTextField) && textFieldIsValid(passwordField);
    }

    private boolean textFieldIsValid(TextField textFieldName) {
        if (textFieldName.getText() != null && textFieldName.getText().isEmpty()) {
            highlightError(textFieldName);
            return false;
        } else return true;
    }

    private void highlightError(TextField textFieldName) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(labels.getString("fillRequiredFields"));
        textFieldName.requestFocus();
    }
}