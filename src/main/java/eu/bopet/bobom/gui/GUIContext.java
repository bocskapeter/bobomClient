package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.entities.Boms;
import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.Users;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GUIContext {
    private final ResourceBundle labels;
    private final Map<Class<?>, ObjectProperty<DBEntities>> selectedPropertyMap;
    private final Map<Class<?>, ObservableList<DBEntities>> entityListMap;
    private final Map<Items, List<Boms>> boms;
    private final String eMail;
    private Clipboard clipboard;
    private Users user;


    public GUIContext(ResourceBundle labels, String eMail) {
        this.labels = labels;
        this.eMail = eMail;
        this.selectedPropertyMap = new HashMap<>();
        this.entityListMap = new HashMap<>();
        this.boms = new HashMap<>();
        clientConnection();
    }

    private void clientConnection() {
        System.out.println("Connect...");
        CountDownLatch messageLatch = new CountDownLatch(1);
        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
        ClientManager client = ClientManager.createClient();
        try {
            URI serverHost = new URI("ws://localhost:8080/bobomServer-1.0-SNAPSHOT/engineering/" + eMail);
            System.out.println("start Endpoint");
            BoMClientEndpoint clientEndpoint = new BoMClientEndpoint(this);
            client.asyncConnectToServer(clientEndpoint, cec, serverHost);
            System.out.println("connected");
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (URISyntaxException | DeploymentException | InterruptedException e) {
            e.printStackTrace();
            putToClipboard(e.getLocalizedMessage());
        }

    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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
