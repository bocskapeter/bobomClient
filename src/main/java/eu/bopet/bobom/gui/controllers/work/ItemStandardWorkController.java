package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.ItemStandards;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.Standards;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ItemStandardWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Label numberLabel;
    @FXML
    private TextField itemNumber;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField itemName;
    @FXML
    private Label revisionLabel;
    @FXML
    private TextField itemRevision;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField itemStatus;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField description;
    @FXML
    private Label itemStandardLabel;
    @FXML
    private TableView<ItemStandards> standardTable;
    @FXML
    private TableColumn<ItemStandards, String> standardNumberColumn;
    @FXML
    private TableColumn<ItemStandards, String> standardNameColumn;
    @FXML
    private TableColumn<ItemStandards, String> descriptionColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<ItemStandards> itemStandards;

    public ItemStandardWorkController(GUIContext context) {
        super(context.getLabels().getString("itemStandard"), ItemStandards.class, context);
        List<ItemStandards> itemStandards = new ArrayList<>();
        this.itemStandards = FXCollections.observableArrayList(itemStandards);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("itemStandard"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        revisionLabel.setText(getContext().getLabels().getString("revision") + Utils.REQUIRED_FIELD);
        statusLabel.setText(getContext().getLabels().getString("status") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);

        itemStandardLabel.setText(getContext().getLabels().getString("standard"));
        standardNumberColumn.setText(getContext().getLabels().getString("number"));
        standardNameColumn.setText(getContext().getLabels().getString("name"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        saveButton.setText(getContext().getLabels().getString("save"));

        standardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().standardNumberProperty());
        standardNameColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().getStandardName().nameProperty(getContext().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().descriptionProperty());

        standardTable.setItems(this.itemStandards);

        errorLabel.setText("");

        itemNumber.setDisable(true);
        itemName.setDisable(true);
        itemRevision.setDisable(true);
        itemStatus.setDisable(true);
        description.setDisable(true);

        getContext().getSelection(Items.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Items item = (Items) newValue;
                this.setCurrentEntity(item);
                itemName.textProperty().set(item.getItemName().getName(getContext().getLanguage()));
                itemNumber.textProperty().set(item.getItemNumber());
                itemRevision.textProperty().set(String.valueOf(item.getRevision()));
                itemStatus.textProperty().set(String.valueOf(item.getStatus()));
                description.textProperty().set(item.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                this.itemStandards.clear();
                List<ItemStandards> standards = getContext().getItemStandards(item);
                if (!standards.isEmpty()) {
                    for (ItemStandards itemStandards : standards) {
                        if (!this.itemStandards.contains(itemStandards)) {
                            this.itemStandards.add(itemStandards);
                        }
                    }
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                }
                standardTable.refresh();
            }
        });
        getContext().getSelection(Standards.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null && getCurrentEntity() != null) {
                Standards standard = (Standards) newValue;
                if (!isOnTheList(standard)) {
                    ItemStandards materialStandard = new ItemStandards();
                    materialStandard.setItem((Items) getCurrentEntity());
                    materialStandard.setStandard(standard);
                    itemStandards.add(materialStandard);
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("objectAlreadyOnList"));
                }
            } else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            }
        });
        clearButton.setOnAction(event -> {
            this.setCurrentEntity(null);
            this.itemStandards.clear();
            itemNumber.textProperty().set("");
            itemName.textProperty().set("");
            itemRevision.textProperty().set("");
            itemStatus.textProperty().set("");
            description.textProperty().set("");
            standardTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });
        saveButton.setOnAction(event -> {
            if (itemStandards.isEmpty() || getCurrentEntity() == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> result = getContext().saveList(itemStandards.stream().collect(Collectors.toList()));
                if (!result.isEmpty()) {
                    itemStandards.clear();
                    for (DBEntities entity : result) {
                        itemStandards.add((ItemStandards) entity);
                    }
                    standardTable.refresh();
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("listSaved"));
                }
            }
        });
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
        standardTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                ItemStandards itemStandard = standardTable.getSelectionModel().getSelectedItem();
                if (itemStandard != null) {
                    if (getContext().deleteEntity(itemStandard)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    this.itemStandards.remove(itemStandard);
                    standardTable.refresh();
                }
            }
        });
    }

    private boolean isOnTheList(Standards standard) {
        for (ItemStandards itemStandard : itemStandards) {
            if (itemStandard.getStandard().equals(standard)) return true;
        }
        return false;
    }
}
