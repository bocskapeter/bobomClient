package eu.bopet.bobom.gui.controllers;

import eu.bopet.bobom.gui.GUIContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainStageController implements Initializable {

    private final GUIContext context;

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Menu userMenu;
    @FXML
    private MenuItem userSettingsMenuItem;
    @FXML
    private MenuItem newUserMenuItem;
    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private AnchorPane mainPane;


    public MainStageController(GUIContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileMenu.setText(context.getLabels().getString("file"));
        userMenu.setText(context.getLabels().getString("user"));
        userSettingsMenuItem.setText(context.getLabels().getString("userSettings"));
        newUserMenuItem.setText(context.getLabels().getString("newUser"));
        helpMenu.setText(context.getLabels().getString("help"));
        aboutMenuItem.setText(context.getLabels().getString("about"));

        closeMenuItem.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        loadEngineeringScene();

        if (context.getUser().getId().toString().equals("330acee3-81ec-45df-9f0d-fe22e46fb36c")) {
            newUserMenuItem.setVisible(true);
        } else newUserMenuItem.setVisible(false);
        userSettingsMenuItem.setOnAction(event -> loadUserSettingsScene());
        newUserMenuItem.setOnAction(event -> loadNewUserScene());
    }

    private void loadNewUserScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewUser.fxml"));
            NewUserController newUserController = new NewUserController(context, this);
            loader.setController(newUserController);
            mainPane.getChildren().clear();
            mainPane.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserSettingsScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserSettings.fxml"));
            UserSettingsController userSettingsController = new UserSettingsController(context, this);
            loader.setController(userSettingsController);
            mainPane.getChildren().clear();
            mainPane.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEngineeringScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EngineeringScene.fxml"));
            EngineeringController engineeringController = new EngineeringController(context);
            loader.setController(engineeringController);
            mainPane.getChildren().clear();
            mainPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
