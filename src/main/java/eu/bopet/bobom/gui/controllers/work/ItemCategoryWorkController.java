package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.Categories;
import eu.bopet.bobom.core.entities.ItemCategories;
import eu.bopet.bobom.core.entities.Items;
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

public class ItemCategoryWorkController extends GUIController implements Initializable {

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
    private Label itemCategoryLabel;
    @FXML
    private TableView<ItemCategories> categoryTable;
    @FXML
    private TableColumn<ItemCategories, String> categoryNameColumn;
    @FXML
    private TableColumn<ItemCategories, String> descriptionColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<ItemCategories> itemCategories;

    public ItemCategoryWorkController(GUIContext context) {
        super(context.getLabels().getString("itemCategory"), ItemCategories.class, context);
        List<ItemCategories> itemCategories = new ArrayList<>();
        this.itemCategories = FXCollections.observableArrayList(itemCategories);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("itemCategory"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        revisionLabel.setText(getContext().getLabels().getString("revision") + Utils.REQUIRED_FIELD);
        statusLabel.setText(getContext().getLabels().getString("status") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);

        itemCategoryLabel.setText(getContext().getLabels().getString("category"));
        categoryNameColumn.setText(getContext().getLabels().getString("name"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        saveButton.setText(getContext().getLabels().getString("save"));

        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().getCategoryName().nameProperty(getContext().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().descriptionProperty());

        categoryTable.setItems(this.itemCategories);

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
                this.itemCategories.clear();
                List<ItemCategories> categories = getContext().getItemCategories(item);
                if (!categories.isEmpty()) {
                    for (ItemCategories itemCategory : categories) {
                        if (!this.itemCategories.contains(itemCategory)) {
                            this.itemCategories.add(itemCategory);
                        }
                    }
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                }
                categoryTable.refresh();
            }
        });*/
        getContext().getSelection(Categories.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null && getCurrentEntity() != null) {
                Categories category = (Categories) newValue;
                if (!isOnTheList(category)) {
                    ItemCategories itemCategory = new ItemCategories();
                    itemCategory.setItem((Items) getCurrentEntity());
                    itemCategory.setCategory(category);
                    this.itemCategories.add(itemCategory);
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
            this.itemCategories.clear();
            itemNumber.textProperty().set("");
            itemName.textProperty().set("");
            itemRevision.textProperty().set("");
            itemStatus.textProperty().set("");
            description.textProperty().set("");
            categoryTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });
/*        saveButton.setOnAction(event -> {
            if (itemCategories.isEmpty() || getCurrentEntity() == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> result = getContext().saveList(itemCategories.stream().collect(Collectors.toList()));
                if (!result.isEmpty()) {
                    itemCategories.clear();
                    for (DBEntities entity : result) {
                        itemCategories.add((ItemCategories) entity);
                    }
                    categoryTable.refresh();
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("listSaved"));
                }
            }
        });*/
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
        categoryTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                ItemCategories itemCategory = categoryTable.getSelectionModel().getSelectedItem();
                if (itemCategory != null) {
                    if (getContext().deleteEntity(itemCategory)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    this.itemCategories.remove(itemCategory);
                    categoryTable.refresh();
                }
            }
        });
    }

    private boolean isOnTheList(Categories category) {
        for (ItemCategories itemCategory : itemCategories) {
            if (itemCategory.getCategory().equals(category)) return true;
        }
        return false;
    }
}
