package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.ItemMaterials;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.Materials;
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

public class ItemMaterialWorkController extends GUIController implements Initializable {

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
    private Label itemMaterialLabel;
    @FXML
    private TableView<ItemMaterials> materialTable;
    @FXML
    private TableColumn<ItemMaterials, String> materialNumberColumn;
    @FXML
    private TableColumn<ItemMaterials, String> materialNameColumn;
    @FXML
    private TableColumn<ItemMaterials, String> descriptionColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<ItemMaterials> itemMaterials;

    public ItemMaterialWorkController(GUIContext context) {
        super(context.getLabels().getString("itemMaterial"), ItemMaterials.class, context);
        List<ItemMaterials> itemMaterials = new ArrayList<>();
        this.itemMaterials = FXCollections.observableArrayList(itemMaterials);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("itemMaterial"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        revisionLabel.setText(getContext().getLabels().getString("revision") + Utils.REQUIRED_FIELD);
        statusLabel.setText(getContext().getLabels().getString("status") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);

        itemMaterialLabel.setText(getContext().getLabels().getString("material"));
        materialNumberColumn.setText(getContext().getLabels().getString("number"));
        materialNameColumn.setText(getContext().getLabels().getString("name"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        saveButton.setText(getContext().getLabels().getString("save"));

        materialNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterial().materialNumberProperty());
        materialNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterial().getMaterialName().nameProperty(getContext().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterial().descriptionProperty());

        materialTable.setItems(this.itemMaterials);

        errorLabel.setText("");

        itemNumber.setDisable(true);
        itemName.setDisable(true);
        itemRevision.setDisable(true);
        itemStatus.setDisable(true);
        description.setDisable(true);

/*        getContext().getSelection(Items.class).addListener((observable, oldValue, newValue) -> {
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
                this.itemMaterials.clear();
                List<ItemMaterials> materials = getContext().getItemMaterials(item);
                if (!materials.isEmpty()) {
                    for (ItemMaterials itemMaterial : materials) {
                        if (!this.itemMaterials.contains(itemMaterial)) {
                            this.itemMaterials.add(itemMaterial);
                        }
                    }
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                }
                materialTable.refresh();
            }
        });*/
        getContext().getSelection(Materials.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null && getCurrentEntity() != null) {
                Materials material = (Materials) newValue;
                if (!isOnTheList(material)) {
                    ItemMaterials itemMaterial = new ItemMaterials();
                    itemMaterial.setItem((Items) getCurrentEntity());
                    itemMaterial.setMaterial(material);
                    this.itemMaterials.add(itemMaterial);
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
            this.itemMaterials.clear();
            itemNumber.textProperty().set("");
            itemName.textProperty().set("");
            itemRevision.textProperty().set("");
            itemStatus.textProperty().set("");
            description.textProperty().set("");
            materialTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });
/*        saveButton.setOnAction(event -> {
            if (itemMaterials.isEmpty() || getCurrentEntity() == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> result = getContext().saveList(itemMaterials.stream().collect(Collectors.toList()));
                if (!result.isEmpty()) {
                    itemMaterials.clear();
                    for (DBEntities entity : result) {
                        itemMaterials.add((ItemMaterials) entity);
                    }
                    materialTable.refresh();
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("listSaved"));
                }
            }
        });*/
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
        materialTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                ItemMaterials itemMaterial = materialTable.getSelectionModel().getSelectedItem();
                if (itemMaterial != null) {
                    if (getContext().deleteEntity(itemMaterial)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    this.itemMaterials.remove(itemMaterial);
                    materialTable.refresh();
                }
            }
        });
    }

    private boolean isOnTheList(Materials material) {
        for (ItemMaterials itemMaterial : itemMaterials) {
            if (itemMaterial.getMaterial().equals(material)) return true;
        }
        return false;
    }
}
