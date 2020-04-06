package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.Categories;
import eu.bopet.bobom.core.entities.CategoryParameters;
import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CategoriesSearchController extends GUIController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DBEntities> categoryTable;
    @FXML
    private TableColumn<Categories, String> categoryNameColumn;
    @FXML
    private TableColumn<Categories, String> categoryGroupColumn;
    @FXML
    private TableColumn<Categories, String> descriptionColumn;
    @FXML
    private Label parametersLabel;
    @FXML
    private TableView<CategoryParameters> categoryParametersTable;
    @FXML
    private TableColumn<CategoryParameters, Integer> parameterSeqColumn;
    @FXML
    private TableColumn<CategoryParameters, String> parameterNameColumn;
    @FXML
    private TableColumn<CategoryParameters, String> parameterUnitColumn;
    @FXML
    private TableColumn<CategoryParameters, String> parameterDescriptionColumn;


    public <T> CategoriesSearchController(GUIContext context) {
        super(context.getLabels().getString("categories"), Categories.class, context);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchLabel.setText(getContext().getLabels().getString("searchFor"));
        categoryNameColumn.setText(getContext().getLabels().getString("name"));
        categoryGroupColumn.setText(getContext().getLabels().getString("categoryGroup"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));
        parametersLabel.setText(getContext().getLabels().getString("parameters"));
        parameterSeqColumn.setText(getContext().getLabels().getString("position"));
        parameterNameColumn.setText(getContext().getLabels().getString("name"));
        parameterUnitColumn.setText(getContext().getLabels().getString("unit"));
        parameterDescriptionColumn.setText(getContext().getLabels().getString("description"));

        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryName().nameEnProperty());
        categoryGroupColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryGroup().getCategoryGroupName().nameEnProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        parameterSeqColumn.setCellValueFactory(cellData -> cellData.getValue().seqProperty().asObject());
        parameterSeqColumn.setSortType(TableColumn.SortType.ASCENDING);
        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterName().nameProperty(getContext().getLanguage()));
        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterUnit().getUnitName().nameProperty(getContext().getLanguage()));
        parameterDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(getEntityClass()), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities categories) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Categories) categories).getDescription() != null && ((Categories) categories).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Categories) categories).getCategoryGroup().getCategoryGroupName().getName(getContext().getLanguage()) != null && ((Categories) categories).getCategoryGroup().getCategoryGroupName().getName(getContext().getLanguage()).toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Categories) categories).getCategoryName().getName(getContext().getLanguage()) != null && ((Categories) categories).getCategoryName().getName(getContext().getLanguage()).toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(categoryTable.comparatorProperty());
        categoryTable.setItems(sortedData);

        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Categories category = (Categories) newSelection;
                getContext().select(getEntityClass(), category);
                setCurrentEntity(category);
                List<CategoryParameters> parameters = getContext().getCategoryParameters(category);
                ObservableList<CategoryParameters> categoryParameters = FXCollections.observableArrayList(parameters);
                categoryParametersTable.setItems(categoryParameters);
                categoryParametersTable.getSortOrder().add(parameterSeqColumn);
            }
        });
        categoryTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                categoryTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        categoryTable.setRowFactory(tv -> {
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
