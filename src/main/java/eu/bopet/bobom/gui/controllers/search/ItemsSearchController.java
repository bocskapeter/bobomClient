package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.*;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import eu.bopet.bobom.gui.controllers.work.ItemParameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemsSearchController extends GUIController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DBEntities> itemsTable;
    @FXML
    private TableColumn<Items, String> itemNumberColumn;
    @FXML
    private TableColumn<Items, String> itemNameColumn;
    @FXML
    private TableColumn<Items, Integer> revisionColumn;
    @FXML
    private TableColumn<Items, Integer> statusColumn;
    @FXML
    private TableColumn<Items, String> descriptionColumn;
    @FXML
    private Label materialLabel;
    @FXML
    private TableView<ItemMaterials> materialsTable;
    @FXML
    private TableColumn<ItemMaterials, String> materialNameColumn;
    @FXML
    private TableColumn<ItemMaterials, String> materialNumberColumn;
    @FXML
    private TableColumn<ItemMaterials, String> materialDescriptionColumn;
    @FXML
    private Label standardLabel;
    @FXML
    private TableView<ItemStandards> standardTable;
    @FXML
    private TableColumn<ItemStandards, String> standardNumberColumn;
    @FXML
    private TableColumn<ItemStandards, String> standardNameColumn;
    @FXML
    private TableColumn<ItemStandards, String> standardDescriptionColumn;
    @FXML
    private Label categoryLabel;
    @FXML
    private TableView<ItemCategories> categoryTable;
    @FXML
    private TableColumn<ItemCategories, String> categoryNameColumn;
    @FXML
    private TableColumn<ItemCategories, String> categoryGroupColumn;
    @FXML
    private TableColumn<ItemCategories, String> categoryDescriptionColumn;
    @FXML
    private Label parameterLabel;
    @FXML
    private TableView<ItemParameter> parameterTable;
    @FXML
    private TableColumn<ItemParameter, String> parameterCategoryColumn;
    @FXML
    private TableColumn<ItemParameter, Integer> parameterSeqColumn;
    @FXML
    private TableColumn<ItemParameter, String> parameterNameColumn;
    @FXML
    private TableColumn<ItemParameter, BigDecimal> parameterValueColumn;
    @FXML
    private TableColumn<ItemParameter, String> parameterUnitColumn;

    private ObservableList<ItemMaterials> itemMaterials;
    private ObservableList<ItemStandards> itemStandards;
    private ObservableList<ItemCategories> itemCategories;
    private ObservableList<ItemParameter> parameterValues;


    public ItemsSearchController(GUIContext context) {
        super(context.getLabels().getString("items"), Items.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchLabel.setText(getContext().getLabels().getString("searchFor"));
        itemNumberColumn.setText(getContext().getLabels().getString("number"));
        itemNameColumn.setText(getContext().getLabels().getString("name"));
        revisionColumn.setText(getContext().getLabels().getString("revision"));
        statusColumn.setText(getContext().getLabels().getString("status"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().getItemName().nameProperty(getContext().getLanguage()));
        itemNumberColumn.setCellValueFactory(cellData -> cellData.getValue().itemNumberProperty());
        revisionColumn.setCellValueFactory(cellData -> cellData.getValue().revisionProperty().asObject());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        materialLabel.setText(getContext().getLabels().getString("material"));
        materialNameColumn.setText(getContext().getLabels().getString("name"));
        materialNumberColumn.setText(getContext().getLabels().getString("number"));
        materialDescriptionColumn.setText(getContext().getLabels().getString("description"));

        materialNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterial().getMaterialName().nameProperty(getContext().getLanguage()));
        materialNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterial().materialNumberProperty());
        materialDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterial().descriptionProperty());

        standardLabel.setText(getContext().getLabels().getString("standard"));
        standardNumberColumn.setText(getContext().getLabels().getString("number"));
        standardNameColumn.setText(getContext().getLabels().getString("name"));
        standardDescriptionColumn.setText(getContext().getLabels().getString("description"));

        standardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().standardNumberProperty());
        standardNameColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().getStandardName().nameProperty(getContext().getLanguage()));
        standardDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().descriptionProperty());

        categoryLabel.setText(getContext().getLabels().getString("category"));
        categoryNameColumn.setText(getContext().getLabels().getString("name"));
        categoryDescriptionColumn.setText(getContext().getLabels().getString("description"));

        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().getCategoryName().nameProperty(getContext().getLanguage()));
        categoryGroupColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().getCategoryGroup().getCategoryGroupName().nameProperty(getContext().getLanguage()));
        categoryDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().descriptionProperty());

        parameterLabel.setText(getContext().getLabels().getString("parameter"));
        parameterCategoryColumn.setText(getContext().getLabels().getString("category"));
        parameterSeqColumn.setText(getContext().getLabels().getString("position"));
        parameterNameColumn.setText(getContext().getLabels().getString("name"));
        parameterValueColumn.setText(getContext().getLabels().getString("value"));
        parameterUnitColumn.setText(getContext().getLabels().getString("unit"));
//
//        parameterCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryParameter().getCategory().getCategoryName().nameProperty(getContext().getLanguage()));
//        parameterSeqColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryParameter().seqProperty().asObject());
//        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterValue().getParameter().getParameterName().nameProperty(getContext().getLanguage()));
//        parameterValueColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterValue().valProperty());
//        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterValue().getParameter().getParameterUnit().getUnitName().nameProperty(getContext().getLanguage()));

        List<ItemMaterials> materials = new ArrayList<>();
        itemMaterials = FXCollections.observableArrayList(materials);
        List<ItemStandards> standards = new ArrayList<>();
        itemStandards = FXCollections.observableArrayList(standards);
        List<ItemCategories> categories = new ArrayList<>();
        itemCategories = FXCollections.observableArrayList(categories);
        List<ItemParameter> parameters = new ArrayList<>();
        parameterValues = FXCollections.observableArrayList(parameters);

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(Items.class), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities items) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Items) items).getItemNumber() != null && ((Items) items).getItemNumber().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Items) items).getDescription() != null && ((Items) items).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Items) items).getItemName().getNameEn() != null && ((Items) items).getItemName().getNameEn().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemsTable.comparatorProperty());
        itemsTable.setItems(sortedData);

/*        itemsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Items item = (Items) newSelection;
                getContext().select(getEntityClass(), item);
                setCurrentEntity(item);
                itemMaterials.clear();
                itemMaterials.addAll(getContext().getItemMaterials(item));
                materialsTable.setItems(itemMaterials);
                itemStandards.clear();
                itemStandards.addAll(getContext().getItemStandards(item));
                standardTable.setItems(itemStandards);
                itemCategories.clear();
                itemCategories.addAll(getContext().getItemCategories(item));
                categoryTable.setItems(itemCategories);

                if (!itemCategories.isEmpty()) {
                    parameterValues.clear();
                    for (ItemCategories ic : itemCategories) {
                        List<CategoryParameters> cps = getContext().getCategoryParameters(ic.getCategory());
                        List<ParameterValues> pvs = getContext().getItemParameters(ic);
                        if (!pvs.isEmpty()) {
                            for (CategoryParameters cp : cps) {
                                Parameters p1 = cp.getParameter();
                                ParameterValues parameterValue = getParameterValue(pvs, p1);
                                if (parameterValue != null) {
                                    try {
                                        ItemParameter itemParameter = new ItemParameter(cp, parameterValue);
                                        parameterValues.add(itemParameter);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    parameterTable.setItems(parameterValues);
                }
            }
        });*/
        itemsTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                itemsTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
 /*       itemsTable.setRowFactory(tv -> {
            TableRow<DBEntities> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if ((event.getClickCount() == 2) && (!row.isEmpty())) {
                    getContext().select(getEntityClass(), null);
                    getContext().select(getEntityClass(), row.getItem());
                }
            });
            return row;
        });*/
    }

 /*   private ParameterValues getParameterValue(List<ParameterValues> pValues, Parameters p1) {
        for (ParameterValues parameterValues : pValues) {
            if (parameterValues.getParameter().equals(p1)) {
                return parameterValues;
            }
        }
        return null;
    }*/
}
