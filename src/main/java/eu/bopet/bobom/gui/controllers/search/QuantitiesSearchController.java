package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Quantities;
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

public class QuantitiesSearchController extends GUIController implements Initializable {

    @FXML
    private TableView<DBEntities> quantitiesTable;
    @FXML
    private TextField filterField;
    @FXML
    private TableColumn<Quantities, String> quantityNameColumn;
    @FXML
    private TableColumn<Quantities, String> descriptionColumn;

    public QuantitiesSearchController(GUIContext context) {
        super(context.getLabels().getString("quantities"), Quantities.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantityNameColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityName().nameEnProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(Quantities.class), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities quantities) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Quantities) quantities).getDescription() != null && ((Quantities) quantities).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Quantities) quantities).getQuantityName().getNameEn() != null && ((Quantities) quantities).getQuantityName().getNameEn().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(quantitiesTable.comparatorProperty());
        quantitiesTable.setItems(sortedData);

        quantitiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);
            }
        });
        quantitiesTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                quantitiesTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        quantitiesTable.setRowFactory(tv -> {
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
