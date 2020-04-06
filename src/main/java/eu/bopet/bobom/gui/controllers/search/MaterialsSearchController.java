package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.MaterialParameters;
import eu.bopet.bobom.core.entities.MaterialStandards;
import eu.bopet.bobom.core.entities.Materials;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class MaterialsSearchController extends GUIController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DBEntities> materialsTable;
    @FXML
    private TableColumn<Materials, String> materialNameColumn;
    @FXML
    private TableColumn<Materials, String> materialNumberColumn;
    @FXML
    private TableColumn<Materials, String> descriptionColumn;
    @FXML
    private Label standardsLabel;
    @FXML
    private TableView<MaterialStandards> materialStandardsTable;
    @FXML
    private TableColumn<MaterialStandards, String> standardNumberColumn;
    @FXML
    private TableColumn<MaterialStandards, String> standardNameColumn;
    @FXML
    private TableColumn<MaterialStandards, String> standardDescriptionColumn;
    @FXML
    private Label parametersLabel;
    @FXML
    private TableView<MaterialParameters> materialParametersTable;
    @FXML
    private TableColumn<MaterialParameters, Integer> parameterSeqColumn;
    @FXML
    private TableColumn<MaterialParameters, String> parameterNameColumn;
    @FXML
    private TableColumn<MaterialParameters, BigDecimal> parameterValueColumn;
    @FXML
    private TableColumn<MaterialParameters, String> parameterUnitColumn;
    @FXML
    private TableColumn<MaterialParameters, String> parameterDescriptionColumn;

    public MaterialsSearchController(GUIContext context) {
        super(context.getLabels().getString("materials"), Materials.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchLabel.setText(getContext().getLabels().getString("searchFor"));
        materialNameColumn.setText(getContext().getLabels().getString("name"));
        materialNumberColumn.setText(getContext().getLabels().getString("number"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        materialNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMaterialName().nameProperty(getContext().getLanguage()));
        materialNumberColumn.setCellValueFactory(cellData -> cellData.getValue().materialNumberProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        standardsLabel.setText(getContext().getLabels().getString("standards"));
        standardNumberColumn.setText(getContext().getLabels().getString("number"));
        standardNameColumn.setText(getContext().getLabels().getString("name"));
        standardDescriptionColumn.setText(getContext().getLabels().getString("description"));

        standardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().standardNumberProperty());
        standardNameColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().getStandardName().nameProperty(getContext().getLanguage()));
        standardDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().descriptionProperty());

        parametersLabel.setText(getContext().getLabels().getString("parameters"));
        parameterSeqColumn.setText(getContext().getLabels().getString("position"));
        parameterNameColumn.setText(getContext().getLabels().getString("name"));
        parameterValueColumn.setText(getContext().getLabels().getString("value"));
        parameterUnitColumn.setText(getContext().getLabels().getString("unit"));
        parameterDescriptionColumn.setText(getContext().getLabels().getString("description"));

        parameterSeqColumn.setCellValueFactory(cellData -> cellData.getValue().seqProperty().asObject());
        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterName().nameProperty(getContext().getLanguage()));
        parameterValueColumn.setCellValueFactory(cellData -> cellData.getValue().valProperty());
        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterUnit().getUnitName().nameProperty(getContext().getLanguage()));
        parameterDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(Materials.class), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities materials) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Materials) materials).getDescription() != null && ((Materials) materials).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Materials) materials).getMaterialNumber() != null && ((Materials) materials).getMaterialNumber().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Materials) materials).getMaterialName().getName(getContext().getLanguage()) != null && ((Materials) materials).getMaterialName().getName(getContext().getLanguage()).toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(materialsTable.comparatorProperty());
        materialsTable.setItems(sortedData);

   /*     materialsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Materials material = (Materials) newSelection;
                getContext().select(getEntityClass(), material);
                setCurrentEntity(material);
                List<MaterialStandards> materialStandards = getContext().getMaterialStandards(material);
                ObservableList<MaterialStandards> standards = FXCollections.observableArrayList(materialStandards);
                materialStandardsTable.setItems(standards);
                List<MaterialParameters> materialParameters = getContext().getMaterialParameter(material);
                ObservableList<MaterialParameters> parameters = FXCollections.observableArrayList(materialParameters);
                materialParametersTable.setItems(parameters);
            }
        });*/
        materialsTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                materialsTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        materialsTable.setRowFactory(tv -> {
            TableRow<DBEntities> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if ((event.getClickCount() == 2) && (!row.isEmpty())) {
                    getContext().select(getEntityClass(), null);
                    getContext().select(getEntityClass(), row.getItem());
                }
            });
            return row;
        });
    }
}
