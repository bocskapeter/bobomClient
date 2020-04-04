package eu.bopet.bobom.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    private ResourceBundle labels;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Locale currentLocale = Locale.getDefault();
            labels = ResourceBundle.getBundle("text/LabelsBundle", currentLocale, new UTF8Control());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        LoginController loginController = new LoginController(stage, labels);
        loader.setController(loginController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/images/bobom.png"));
        stage.show();
    }
}
