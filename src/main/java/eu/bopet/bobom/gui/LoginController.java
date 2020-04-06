package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMActivity;
import eu.bopet.bobom.core.BoMMessage;
import eu.bopet.bobom.core.entities.Users;
import eu.bopet.bobom.gui.controllers.MainStageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.Arrays;
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
        this.context = new GUIContext(labels);

        loginButton.setOnKeyPressed(event -> fireLogin());
        loginButton.setOnAction(event -> fireLogin());

        this.context.userProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Users user = newValue;
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(labels.getString("checking"));
                if (checkPwd(passwordField.getText(), user.getPassword()))showApp(stage, user);
            } else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(labels.getString("errorUnknownUser"));
                clearFields();
            }
        });
    }

    private void fireLogin() {
        if(context.isConnected()){
            if (validate()) {
                attemptLoginMessage();
                String eMail = eMailTextField.getText();
                if (this.context.getUser()!=null && this.context.getUser().getEMail().equals(eMail)){
                    Users user = context.getUser();
                    if (checkPwd(passwordField.getText(), user.getPassword()))showApp(stage, user);
                } else {
                    BoMMessage message = new BoMMessage(BoMActivity.LOGIN,Users.class,null, Arrays.asList(eMail));
                    this.context.sendMessage(message);
                }
            }
        }

    }

    private boolean checkPwd(String field, String pwd){
        if (BCrypt.checkpw(field, pwd)) {
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(labels.getString("waitLoadingApp"));
            clearFields();
            return true;
        } else {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText(labels.getString("errorWrongPassword"));
            passwordField.textProperty().set("");
            passwordField.requestFocus();
            return false;
        }
    }

    private void showApp(Stage stage, Users user) {
        try {
            labels = ResourceBundle.getBundle("text/LabelsBundle", user.getLocale(), new UTF8Control());
            MainStageController mainStageController = new MainStageController(context);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScene.fxml"));
            loader.setController(mainStageController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setTitle("BoBoM");
            stage.getIcons().add(new Image("/images/bobom.png"));
            stage.setX(10);
            stage.setY(10);
            stage.setScene(scene);
            stage.show();
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