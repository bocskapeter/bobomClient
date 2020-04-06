package eu.bopet.bobom.gui.controllers;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.gui.GUIContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;

public abstract class GUIController {

    private final String displayName;
    private final GUIContext context;
    private final Class<DBEntities> entityClass;
    private final StringProperty status;
    private BorderPane pane;
    private DBEntities currentEntity;

    public <T> GUIController(String displayName, Class<T> entityClass, GUIContext context) {
        this.displayName = displayName;
        this.entityClass = (Class<DBEntities>) entityClass;
        this.context = context;
        status = new SimpleStringProperty();
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<DBEntities> getEntityClass() {
        return entityClass;
    }

    public BorderPane getPane() {
        return pane;
    }

    public void setPane(BorderPane pane) {
        this.pane = pane;
    }

    public GUIContext getContext() {
        return context;
    }

    public DBEntities getCurrentEntity() {
        return currentEntity;
    }

    public void setCurrentEntity(DBEntities currentEntity) {
        this.currentEntity = currentEntity;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
