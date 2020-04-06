package eu.bopet.bobom.gui.controllers.search;

import eu.bopet.bobom.core.entities.Boms;
import eu.bopet.bobom.core.entities.DBEntities;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.Units;
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BomsSearchController extends GUIController implements Initializable {

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
    private TreeTableView<Boms> bomTreeTable;
    @FXML
    private TreeTableColumn<Boms, Integer> treeSeq;
    @FXML
    private TreeTableColumn<Boms, String> treeComponentName;
    @FXML
    private TreeTableColumn<Boms, String> treeComponentNumber;
    @FXML
    private TreeTableColumn<Boms, BigDecimal> treeQuantity;
    @FXML
    private TreeTableColumn<Boms, String> treeUnit;

    public BomsSearchController(GUIContext context) {
        super(context.getLabels().getString("boms"), Boms.class, context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchLabel.setText(getContext().getLabels().getString("searchFor"));
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().getItemName().nameProperty(getContext().getLanguage()));
        itemNumberColumn.setCellValueFactory(cellData -> cellData.getValue().itemNumberProperty());
        revisionColumn.setCellValueFactory(cellData -> cellData.getValue().revisionProperty().asObject());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        List<DBEntities> boms = getContext().getAllData(Boms.class);
        List<Items> bomList = new ArrayList<>();
        for (DBEntities b : boms) {
            if (!bomList.contains(((Boms) b).getAssembly())) {
                bomList.add(((Boms) b).getAssembly());
            }
        }
        ObservableList<DBEntities> list = FXCollections.observableArrayList(bomList);

        FilteredList<DBEntities> filteredData = new FilteredList<>(list, p -> true);
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
                return ((Items) items).getItemName().getName(getContext().getLanguage()) != null && ((Items) items).getItemName().getName(getContext().getLanguage()).toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<DBEntities> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemsTable.comparatorProperty());
        itemsTable.setItems(sortedData);

        itemsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getContext().select(getEntityClass(), newSelection);
                setCurrentEntity(newSelection);

                List<Boms> bom = getContext().getBom((Items) newSelection);

                Boms assembly = new Boms();
                assembly.setComponent(bom.get(0).getAssembly());
                assembly.setSeq(0);
                assembly.setQuantity(new BigDecimal("1.0"));
                Units each = (Units) getContext().getEntityByUID(Units.class, "453cf498-429f-4b68-9801-16655e4a3b40");
                assembly.setUnit(each);

                TreeItem<Boms> root = new TreeItem<>(assembly);

                getPositions(root, bom);

                bomTreeTable.setRoot(root);

                root.setExpanded(true);

                treeSeq.setCellValueFactory(cellData -> cellData.getValue().getValue().seqProperty().asObject());
                treeComponentNumber.setCellValueFactory(cellData -> cellData.getValue().getValue().getComponent().itemNumberProperty());
                treeComponentName.setCellValueFactory(cellData -> cellData.getValue().getValue().getComponent().getItemName().nameProperty(getContext().getLanguage()));
                treeQuantity.setCellValueFactory(cellData -> cellData.getValue().getValue().quantityProperty());
                treeUnit.setCellValueFactory(cellData -> cellData.getValue().getValue().getUnit().getUnitName().nameProperty(getContext().getLanguage()));
            }
        });
        itemsTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                itemsTable.getSelectionModel().clearSelection();
                getContext().getSelection(getEntityClass()).set(null);
            }
        });
        itemsTable.setRowFactory(tv -> {
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

    private void getPositions(TreeItem<Boms> root, List<Boms> bom) {
        for (Boms b : bom) {
            TreeItem<Boms> child = new TreeItem<>(b);
            root.getChildren().add(child);
            List<Boms> subBom = getContext().getBom(b.getComponent());
            if (!subBom.isEmpty()) {
                getPositions(child, subBom);
                child.setExpanded(true);
            }
        }
    }
}
