package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.names.Names;
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


public abstract class NamesSearchController extends GUIController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DBEntities> nameTable;
    @FXML
    private TableColumn<Names, String> nameEnColumn;
    @FXML
    private TableColumn<Names, String> nameDeColumn;
    @FXML
    private TableColumn<Names, String> nameHuColumn;

    public NamesSearchController(String name, Class<?> namesClass, GUIContext context) {
        super(name, namesClass, context);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        searchLabel.setText(getContext().getLabels().getString("searchFor"));
        nameEnColumn.setText(getContext().getLabels().getString("name") + " - " + getContext().getLabels().getString("english"));
        nameDeColumn.setText(getContext().getLabels().getString("name") + " - " + getContext().getLabels().getString("german"));
        nameHuColumn.setText(getContext().getLabels().getString("name") + " - " + getContext().getLabels().getString("hungarian"));

        nameEnColumn.setCellValueFactory(cellData -> cellData.getValue().nameEnProperty());
        nameDeColumn.setCellValueFactory(cellData -> cellData.getValue().nameDeProperty());
        nameHuColumn.setCellValueFactory(cellData -> cellData.getValue().nameHuProperty());

        nameTable.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().equals(KeyCode.C)) {
                Names selection = (Names) getCurrentEntity();
                if (selection != null) {
                    String s = selection.getNameEn() + "\t" + selection.getNameDe() + "\t" + selection.getNameHu();
                    getContext().putToClipboard(s);
                }
            }
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                nameTable.getSelectionModel().clearSelection();
                getContext().select(getEntityClass(), null);
                setCurrentEntity(null);
            }
        });

        FilteredList<DBEntities> filteredData = new FilteredList<>(getContext().getAllData(getEntityClass()), p -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate((DBEntities names) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (((Names) names).getNameEn() != null && ((Names) names).getNameEn().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (((Names) names).getNameDe() != null && ((Names) names).getNameDe().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return ((Names) names).getNameHu() != null && ((Names) names).getNameHu().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(nameTable.comparatorProperty());
        nameTable.setItems(sortedData);

        nameTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);
            }
        });
        nameTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                nameTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        nameTable.setRowFactory(tv -> {
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
