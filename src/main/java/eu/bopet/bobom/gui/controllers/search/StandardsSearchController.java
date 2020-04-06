package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Standards;
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


public class StandardsSearchController extends GUIController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DBEntities> standardTable;
    @FXML
    private TableColumn<Standards, String> standardNumberColumn;
    @FXML
    private TableColumn<Standards, String> standardNameColumn;
    @FXML
    private TableColumn<Standards, String> descriptionColumn;

    public <T> StandardsSearchController(GUIContext context) {
        super(context.getLabels().getString("standards"), Standards.class, context);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        standardNameColumn.setCellValueFactory(cellData -> cellData.getValue().getStandardName().nameEnProperty());
        standardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().standardNumberProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(getEntityClass()), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities standard) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Standards) standard).getDescription() != null && ((Standards) standard).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Standards) standard).getStandardNumber() != null && ((Standards) standard).getStandardNumber().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Standards) standard).getStandardName().getNameEn() != null && ((Standards) standard).getStandardName().getNameEn().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(standardTable.comparatorProperty());
        standardTable.setItems(sortedData);

        standardTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);
            }
        });
        standardTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                standardTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        standardTable.setRowFactory(tv -> {
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
