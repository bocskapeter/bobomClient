package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Units;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;


public class UnitsSearchController extends GUIController implements Initializable {

    @FXML
    private TableView<DBEntities> unitTable;
    @FXML
    private TextField filterField;
    @FXML
    private TableColumn<Units, String> unitNameColumn;
    @FXML
    private TableColumn<Units, String> unitQuantityColumn;
    @FXML
    private TableColumn<Units, String> descriptionColumn;

    public <T> UnitsSearchController(GUIContext context) {
        super(context.getLabels().getString("units"), Units.class, context);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        unitNameColumn.setCellValueFactory(cellData -> cellData.getValue().getUnitName().nameProperty(getContext().getLanguage()));
        unitQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getUnitQuantity().getQuantityName().nameEnProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(getEntityClass()), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities units) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Units) units).getDescription() != null && ((Units) units).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Units) units).getUnitQuantity().getQuantityName().getNameEn() != null && ((Units) units).getUnitQuantity().getQuantityName().getNameEn().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Units) units).getUnitName().getNameEn() != null && ((Units) units).getUnitName().getNameEn().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(unitTable.comparatorProperty());
        unitTable.setItems(sortedData);

        unitTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);
            }
        });
        unitTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                unitTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        unitTable.setRowFactory(tv -> {
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
