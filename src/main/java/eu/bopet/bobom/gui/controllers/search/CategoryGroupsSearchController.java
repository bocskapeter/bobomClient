package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.CategoryGroups;
import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryGroupsSearchController extends GUIController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DBEntities> categoryGroupsTable;
    @FXML
    private TableColumn<CategoryGroups, String> categoryGroupsNameColumn;
    @FXML
    private TableColumn<CategoryGroups, String> descriptionColumn;

    public CategoryGroupsSearchController(GUIContext context) {
        super(context.getLabels().getString("categoryGroups"), CategoryGroups.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchLabel.setText(getContext().getLabels().getString("searchFor"));
        categoryGroupsNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryGroupName().nameProperty(getContext().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(CategoryGroups.class), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities categoryGroup) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((CategoryGroups) categoryGroup).getDescription() != null && ((CategoryGroups) categoryGroup).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((CategoryGroups) categoryGroup).getCategoryGroupName().getName(getContext().getLanguage()) != null && ((CategoryGroups) categoryGroup).getCategoryGroupName().getName(getContext().getLanguage()).toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(categoryGroupsTable.comparatorProperty());
        categoryGroupsTable.setItems(sortedData);

        categoryGroupsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);
            }
        });
        categoryGroupsTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                categoryGroupsTable.getSelectionModel().clearSelection();
                getContext().select(getEntityClass(), null);
            }
        });
        categoryGroupsTable.setRowFactory(tv -> {
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
