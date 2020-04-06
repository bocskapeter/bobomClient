package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.Boms;
import eu.bopet.bobom.core.entities.Items;
import eu.bopet.bobom.core.entities.Units;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BomWorkController extends GUIController implements Initializable {

    private final ObjectProperty<Boms> selectedBomPosition;
    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private TextField assemblyNumber;
    @FXML
    private TextField assemblyName;
    @FXML
    private TableView<Boms> componentsTable;
    private TableColumn<Boms, Integer> componentSeqColumn;
    private TableColumn<Boms, String> componentNumberColumn;
    private TableColumn<Boms, String> componentNameColumn;
    private TableColumn<Boms, BigDecimal> quantityColumn;
    private TableColumn<Boms, String> unitNameColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<Boms> bomData;

    public BomWorkController(GUIContext context) {
        super(context.getLabels().getString("bom"), Boms.class, context);
        bomData = FXCollections.observableArrayList();
        selectedBomPosition = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("boms"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));
        saveButton.setText(getContext().getLabels().getString("save"));

        assemblyName.setDisable(true);
        assemblyNumber.setDisable(true);

        componentsTable.setItems(bomData);
        componentsTable.setEditable(true);

        componentSeqColumn = new TableColumn<>(getContext().getLabels().getString("position"));
        componentsTable.getColumns().add(componentSeqColumn);
        componentSeqColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        componentSeqColumn.setOnEditCommit((TableColumn.CellEditEvent<Boms, Integer> t) -> t.getTableView().getItems().get(
                t.getTablePosition().getRow()).setSeq(t.getNewValue()));

        componentNumberColumn = new TableColumn<>(getContext().getLabels().getString("number"));
        componentsTable.getColumns().add(componentNumberColumn);

        componentNameColumn = new TableColumn<>(getContext().getLabels().getString("name"));
        componentsTable.getColumns().add(componentNameColumn);

        quantityColumn = new TableColumn<>(getContext().getLabels().getString("quantity"));
        componentsTable.getColumns().add(quantityColumn);

        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        quantityColumn.setOnEditCommit((TableColumn.CellEditEvent<Boms, BigDecimal> t) -> t.getTableView().getItems().get(
                t.getTablePosition().getRow()).setQuantity(t.getNewValue()));

        unitNameColumn = new TableColumn<>(getContext().getLabels().getString("unit"));
        componentsTable.getColumns().add(unitNameColumn);

        componentSeqColumn.setCellValueFactory(cellData -> cellData.getValue().seqProperty().asObject());
        componentNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getComponent().itemNumberProperty());
        componentNameColumn.setCellValueFactory(cellData -> cellData.getValue().getComponent().getItemName().nameProperty(getContext().getLanguage()));
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        unitNameColumn.setCellValueFactory(cellData -> cellData.getValue().getUnit().getUnitName().nameProperty(getContext().getLanguage()));

        componentsTable.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (componentsTable.getSelectionModel().getSelectedItem() != null) {
                selectedBomPosition.setValue(componentsTable.getSelectionModel().getSelectedItem());
            }
        });
        getContext().getSelection(Items.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Items item = (Items) newValue;
                if (getCurrentEntity() == null) {
                    this.setCurrentEntity(item);
                    List<Boms> bom = getContext().getBom(item);
                    if (!bom.isEmpty()) {
                        this.bomData = FXCollections.observableArrayList(bom);
                        componentsTable.refresh();
                    } else {
                        assemblyNumber.textProperty().set(item.getItemNumber());
                        assemblyName.textProperty().set(item.getItemName().getName(getContext().getLanguage()));
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                    }
                } else {
                    Boms bom = new Boms();
                    bom.setAssembly((Items) getCurrentEntity());
                    bom.setComponent(item);
                    bom.setSeq(getMaxSeq() + 1);
                    bom.setQuantity(new BigDecimal("1.0"));
                    if (getContext().getSelection(Units.class) == null || getContext().getSelection(Units.class).get() == null) {
                        Units each = (Units) getContext().getEntityByUID(Units.class, "453cf498-429f-4b68-9801-16655e4a3b40");
                        bom.setUnit(each);
                    } else {
                        Units unit = (Units) getContext().getSelection(Units.class).get();
                        bom.setUnit((unit));
                    }
                    bomData.add(bom);
                    selectedBomPosition.setValue(bom);
                }
            }
        });
        getContext().getSelection(Units.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (this.selectedBomPosition != null && this.selectedBomPosition.get() != null) {
                    this.selectedBomPosition.get().setUnit((Units) newValue);
                    componentsTable.refresh();
                }
            }
        });
        clearButton.setOnAction(event -> {
            this.setCurrentEntity(null);
            bomData.clear();
            clearFields();
            clearFieldsLabel();
        });
        componentsTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                Boms bom = componentsTable.getSelectionModel().getSelectedItem();
                if (bom != null) {
                    if (getContext().deleteEntity(bom)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    componentsTable.getItems().remove(bom);
                    componentsTable.refresh();
                }
            }
        });
        saveButton.setOnAction(event -> {
            if (validate()) {
                List<Boms> savedBom = getContext().saveNewBom(bomData);
                if (savedBom.isEmpty()) {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("errorInStructure"));
                } else {
                    clearFields();
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getDisplayName() + getContext().getLabels().getString("hasBeenSaved"));
                }
            } else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("errorInStructure"));
            }
        });
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
    }

    private void clearFields() {
        assemblyNumber.textProperty().set("");
        assemblyName.textProperty().set("");
        bomData.clear();
    }

    private void clearFieldsLabel() {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
    }

    private boolean validate() {
        List<Integer> positions = new ArrayList<>();
        for (Boms b : bomData) {
            if (positions.contains(b.getSeq())) {
                componentsTable.getSelectionModel().select(b);
                return false;
            }
            positions.add(b.getSeq());
        }
        return true;
    }

    private int getMaxSeq() {
        int result = 0;
        for (Boms bom : bomData) {
            if (bom.getSeq() > result) {
                result = bom.getSeq();
            }
        }
        return result;
    }
}
