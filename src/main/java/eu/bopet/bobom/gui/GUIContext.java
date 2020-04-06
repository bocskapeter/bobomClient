package eu.bopet.bobom.gui;

import eu.bopet.bobom.core.BoMActivity;
import eu.bopet.bobom.core.BoMMessage;
import eu.bopet.bobom.core.entities.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

public class GUIContext {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ResourceBundle labels;
    private final Map<Class<?>, ObjectProperty<DBEntities>> selectedPropertyMap;
    private final Map<Class<?>, ObservableList<DBEntities>> entityListMap;
    private final Map<Items, List<Boms>> boms;
    private boolean isConnected;
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
            URI serverHost = new URI("ws://localhost:8080/bobomServer/engineering");
            clientEndpoint = new BoMClientEndpoint(this);
            client.connectToServer(clientEndpoint, serverHost);
            isConnected = true;
        } catch (URISyntaxException | DeploymentException | IOException e) {
            logger.warning(e.getLocalizedMessage());
            isConnected = false;
        }
    }

    public void sendMessage(BoMMessage message){
        Platform.runLater(() -> clientEndpoint.sendMessage(message));
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

    public boolean isConnected() {
        return isConnected;
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

    public void putIntoEntityList(Class<?> entityClass, List<DBEntities> entities) {
        for (DBEntities entity : entities) {
            if (!entityListMap.get(entityClass).contains(entity)) {
                this.entityListMap.get(entityClass).add(entity);
            }
        }
    }

    public void select(Class<?> selectedClass, DBEntities entity) {
        this.selectedPropertyMap.get(selectedClass).setValue(entity);
    }

    public ObjectProperty<?> getSelection(Class<?> selectedClass) {
        return this.selectedPropertyMap.get(selectedClass);
    }

    public void loadAllData(Class<DBEntities> entityClass) {
        BoMMessage message = new BoMMessage(BoMActivity.READ_ALL, entityClass, user.get(), null);
        sendMessage(message);
    }

    public Users saveNew(Users user) {
        //TODO
        return null;
    }

    public String getLanguage() {
        return user.get().getLocale().getLanguage();
    }

    public List<Boms> getBom(Items newSelection) {
        //TODO
        return null;
    }

    public Object getEntityByUID(Class<Units> unitsClass, String s) {
        //TODO
        return null;
    }

    public List<CategoryParameters> getCategoryParameters(Categories category) {
        //TODO
        return null;
    }

    public boolean deleteEntity(DBEntities entity) {
        //TODO
        return true;
    }

    public List<Boms> saveNewBom(ObservableList<Boms> bomData) {
        //TODO
        return null;
    }

    public DBEntities saveNewEntity(Object[] fields, Class<DBEntities> entityClass) {
        //TODO
        return null;
    }

    public boolean modifyEntity(DBEntities currentEntity, Object[] fields) {
        //TODO
        return false;
    }

    public List<ItemStandards> getItemStandards(Items item) {
        //TODO
        return null;
    }

    public List<DBEntities> saveList(List<ItemStandards> collect) {
        //TODO
        return null;
    }

    public List<ItemCategories> getItemCategories(Items item) {
        //TODO
        return null;
    }

    public List<ParameterValues> getItemParameters(ItemCategories itemCategory) {
        //TODO
        return null;
    }
}
