package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.*;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemParameterWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Label numberLabel;
    @FXML
    private TextField itemNumber;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField itemName;
    @FXML
    private Label revisionLabel;
    @FXML
    private TextField itemRevision;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField itemStatus;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField description;
    @FXML
    private Label itemCategoryLabel;
    @FXML
    private TableView<ItemCategories> categoryTable;
    @FXML
    private TableColumn<ItemCategories, String> categoryNameColumn;
    @FXML
    private TableColumn<ItemCategories, String> descriptionColumn;
    @FXML
    private Label itemParameterLabel;
    @FXML
    private TableView<ItemParameter> parameterTable;
    private TableColumn<ItemParameter, String> parameterCategoryNameColumn;
    private TableColumn<ItemParameter, Integer> parameterSeqColumn;
    private TableColumn<ItemParameter, String> parameterNameColumn;
    private TableColumn<ItemParameter, BigDecimal> parameterValueColumn;
    private TableColumn<ItemParameter, String> parameterUnitColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<ItemCategories> itemCategories;
    private ObservableList<ItemParameter> parameterValues;

    public ItemParameterWorkController(GUIContext context) {
        super(context.getLabels().getString("itemParameter"), ParameterValues.class, context);
        List<ItemCategories> itemCategories = new ArrayList<>();
        this.itemCategories = FXCollections.observableArrayList(itemCategories);
        List<ItemParameter> parameterValues = new ArrayList<>();
        this.parameterValues = FXCollections.observableArrayList(parameterValues);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("itemParameter"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        revisionLabel.setText(getContext().getLabels().getString("revision") + Utils.REQUIRED_FIELD);
        statusLabel.setText(getContext().getLabels().getString("status") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);

        itemNumber.setDisable(true);
        itemName.setDisable(true);
        itemRevision.setDisable(true);
        itemStatus.setDisable(true);
        description.setDisable(true);

        itemCategoryLabel.setText(getContext().getLabels().getString("category"));
        categoryNameColumn.setText(getContext().getLabels().getString("name"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().getCategoryName().nameProperty(getContext().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory().descriptionProperty());

        categoryTable.setItems(this.itemCategories);

        itemParameterLabel.setText(getContext().getLabels().getString("parameters"));
        parameterCategoryNameColumn = new TableColumn<>();
        parameterCategoryNameColumn.setText(getContext().getLabels().getString("category"));
        parameterSeqColumn = new TableColumn<>();
        parameterSeqColumn.setText(getContext().getLabels().getString("position"));
        parameterNameColumn = new TableColumn<>();
        parameterNameColumn.setText(getContext().getLabels().getString("name"));

        parameterValueColumn = new TableColumn<>(getContext().getLabels().getString("value"));
        parameterValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        parameterValueColumn.setOnEditCommit((TableColumn.CellEditEvent<ItemParameter, BigDecimal> t) -> t.getTableView().getItems().get(
                t.getTablePosition().getRow()).getParameterValue().setVal(t.getNewValue()));

        parameterUnitColumn = new TableColumn<>();
        parameterUnitColumn.setText(getContext().getLabels().getString("unit"));

        parameterCategoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryParameter().getCategory().getCategoryName().nameProperty(getContext().getLanguage()));
        parameterSeqColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryParameter().seqProperty().asObject());
        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryParameter().getParameter().getParameterName().nameProperty(getContext().getLanguage()));
        parameterValueColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterValue().valProperty());
        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameterValue().getParameter().getParameterUnit().getUnitName().nameProperty(getContext().getLanguage()));

        parameterTable.getColumns().add(parameterCategoryNameColumn);
        parameterTable.getColumns().add(parameterSeqColumn);
        parameterTable.getColumns().add(parameterNameColumn);
        parameterTable.getColumns().add(parameterValueColumn);
        parameterTable.getColumns().add(parameterUnitColumn);

        parameterTable.setEditable(true);
        parameterTable.setItems(this.parameterValues);

        saveButton.setText(getContext().getLabels().getString("save"));
        errorLabel.setText("");

        getContext().getSelection(Items.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Items item = (Items) newValue;
                this.setCurrentEntity(item);
                itemName.textProperty().set(item.getItemName().getName(getContext().getLanguage()));
                itemNumber.textProperty().set(item.getItemNumber());
                itemRevision.textProperty().set(String.valueOf(item.getRevision()));
                itemStatus.textProperty().set(String.valueOf(item.getStatus()));
                description.textProperty().set(item.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                this.itemCategories.clear();
                this.parameterValues.clear();
                List<ItemCategories> categories = getContext().getItemCategories(item);
                if (!categories.isEmpty()) {
                    for (ItemCategories itemCategory : categories) {
                        if (!this.itemCategories.contains(itemCategory)) {
                            this.itemCategories.add(itemCategory);
                        }
                        List<CategoryParameters> categoryParameters = getContext().getCategoryParameters(itemCategory.getCategory());
                        List<ParameterValues> pValues = getContext().getItemParameters(itemCategory);
                        for (CategoryParameters cp : categoryParameters) {
                            try {
                                if (pValues.isEmpty()) {
                                    ParameterValues pv = new ParameterValues();
                                    pv.setVal(new BigDecimal("0.0"));
                                    pv.setParameter(cp.getParameter());
                                    pv.setItemCategory(itemCategory);
                                    ItemParameter itemParameter = new ItemParameter(cp, pv);
                                    parameterValues.add(itemParameter);

                                } else {
                                    Parameters p1 = cp.getParameter();
                                    ParameterValues parameterValue = getParameterValue(pValues, p1);
                                    if (parameterValue != null) {
                                        ItemParameter itemParameter = new ItemParameter(cp, parameterValue);
                                        parameterValues.add(itemParameter);
                                    } else {
                                        ParameterValues pv = new ParameterValues();
                                        pv.setVal(new BigDecimal("0.0"));
                                        pv.setParameter(cp.getParameter());
                                        pv.setItemCategory(itemCategory);
                                        ItemParameter itemParameter = new ItemParameter(cp, pv);
                                        parameterValues.add(itemParameter);
                                    }
                                }
                            } catch (Exception e) {
                                errorLabel.setTextFill(Color.RED);
                                errorLabel.setText(getContext().getLabels().getString("internalError") + " "
                                        + getContext().getLabels().getString("errorCode") + " "
                                        + e.getMessage() + "\n" + cp.toString());
                            }
                        }
                    }
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("missingCategories"));
                }
                categoryTable.refresh();
                parameterTable.refresh();
            }
        });
        clearButton.setOnAction(event -> {
            this.setCurrentEntity(null);
            this.itemCategories.clear();
            this.parameterValues.clear();
            itemNumber.textProperty().set("");
            itemName.textProperty().set("");
            itemRevision.textProperty().set("");
            itemStatus.textProperty().set("");
            description.textProperty().set("");
            categoryTable.refresh();
            parameterTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });
/*        saveButton.setOnAction(event -> {
            if (itemCategories.isEmpty() || getCurrentEntity() == null || parameterValues.isEmpty()) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> toSave = new ArrayList<>();
                for (ItemParameter ip : parameterValues) {
                    ParameterValues pv = ip.getParameterValue();
                    toSave.add(pv);
                }
                List<DBEntities> result = getContext().saveList(toSave);
                if (!result.isEmpty()) {
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("listSaved"));
                }
            }
        });*/
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
    }

    private ParameterValues getParameterValue(List<ParameterValues> pValues, Parameters p1) {
        for (ParameterValues parameterValues : pValues) {
            if (parameterValues.getParameter().equals(p1)) {
                return parameterValues;
            }
        }
        return null;
    }
}
