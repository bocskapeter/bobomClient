package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Parameters;
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


public class ParametersSearchController extends GUIController implements Initializable {

    @FXML
    private TableView<DBEntities> parameterTable;
    @FXML
    private TextField filterField;
    @FXML
    private TableColumn<Parameters, String> parameterNameColumn;
    @FXML
    private TableColumn<Parameters, String> parameterUnitColumn;
    @FXML
    private TableColumn<Parameters, String> descriptionColumn;

    public <T> ParametersSearchController(GUIContext context) {
        super(context.getLabels().getString("parameters"), Parameters.class, context);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterName().nameEnProperty());
        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterUnit().getUnitName().nameEnProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(getEntityClass()), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities parameters) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Parameters) parameters).getDescription() != null && ((Parameters) parameters).getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Parameters) parameters).getParameterUnit().getUnitName().getNameEn() != null && ((Parameters) parameters).getParameterUnit().getUnitName().getNameEn().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Parameters) parameters).getParameterName().getNameEn() != null && ((Parameters) parameters).getParameterName().getNameEn().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(parameterTable.comparatorProperty());
        parameterTable.setItems(sortedData);

        parameterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);
            }
        });
        parameterTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                parameterTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        parameterTable.setRowFactory(tv -> {
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
