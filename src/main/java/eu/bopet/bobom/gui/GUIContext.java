package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMMessage;
import eu.bopet.bobom.core.entities.Boms;
import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.Users;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class GUIContext {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ResourceBundle labels;
    private final Map<Class<?>, ObjectProperty<DBEntities>> selectedPropertyMap;
    private final Map<Class<?>, ObservableList<DBEntities>> entityListMap;
    private final Map<Items, List<Boms>> boms;
    private Clipboard clipboard;
    private ObjectProperty<Users> user;
    BoMClientEndpoint clientEndpoint;


    public GUIContext(ResourceBundle labels) {
        this.labels = labels;
        this.selectedPropertyMap = new HashMap<>();
        this.entityListMap = new HashMap<>();
        this.boms = new HashMap<>();
        this.user = new SimpleObjectProperty<>();
        Platform.runLater(() -> clientConnection());
    }

    private void clientConnection() {
        ClientManager client = ClientManager.createClient();
        try {
            URI serverHost = new URI("ws://localhost:8080/bobomServer-1.0-SNAPSHOT/engineering");
            clientEndpoint = new BoMClientEndpoint(this);
            client.connectToServer(clientEndpoint, serverHost);
        } catch (URISyntaxException | DeploymentException | IOException e) {
            e.printStackTrace();
            logger.warning(e.getLocalizedMessage());
        }
    }

    public void sendMessage(BoMMessage message){
        clientEndpoint.sendMessage(message);
    }

    public Users getUser() {
        return user.get();
    }

    public ObjectProperty<Users> userProperty() {
        return user;
    }

    public void setUser(Users user) {
        this.user.set(user);
    }

    public ResourceBundle getLabels() {
        return labels;
    }

    public ObservableList<DBEntities> getAllData(Class<?> c) {
        return this.entityListMap.get(c);
    }

    public void putToClipboard(String content) {
        if (clipboard == null) {
            clipboard = Clipboard.getSystemClipboard();
        }
        clipboard.clear();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.put(DataFormat.PLAIN_TEXT, content);
        clipboard.setContent(clipboardContent);
    }

    public void registerEntityClass(Class<?> entityClass) {
        ObjectProperty<DBEntities> objectProperty = new SimpleObjectProperty<>();
        this.selectedPropertyMap.put(entityClass, objectProperty);
        List<DBEntities> listOfAllData = new ArrayList<>();
        this.entityListMap.put(entityClass, FXCollections.observableArrayList(listOfAllData));
    }

    public void select(Class<?> selectedClass, DBEntities entity) {
        this.selectedPropertyMap.get(selectedClass).setValue(entity);
    }

    public ObjectProperty<?> getSelection(Class<?> selectedClass) {
        return this.selectedPropertyMap.get(selectedClass);
    }

}
