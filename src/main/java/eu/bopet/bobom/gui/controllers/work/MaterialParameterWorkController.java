package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.MaterialParameters;
import eu.bopet.bobom.core.entities.Materials;
import eu.bopet.bobom.core.entities.Parameters;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
import eu.bopet.bobom.gui.controllers.GUIController;
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

public class MaterialParameterWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField materialName;
    @FXML
    private Label numberLabel;
    @FXML
    private TextField materialNumber;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField description;
    @FXML
    private Label materialParameterLabel;
    @FXML
    private TableView<MaterialParameters> parameterTable;
    private TableColumn<MaterialParameters, Integer> seqColumn;
    private TableColumn<MaterialParameters, String> parameterNameColumn;
    private TableColumn<MaterialParameters, BigDecimal> parameterValueColumn;
    private TableColumn<MaterialParameters, String> parameterUnitColumn;
    private TableColumn<MaterialParameters, String> descriptionColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<MaterialParameters> materialParameters;

    public MaterialParameterWorkController(GUIContext context) {
        super(context.getLabels().getString("materialParameter"), MaterialParameters.class, context);
        List<MaterialParameters> materialParameters = new ArrayList<>();
        this.materialParameters = FXCollections.observableArrayList(materialParameters);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("materialParameter"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);

        materialParameterLabel.setText(getContext().getLabels().getString("parameter"));
        seqColumn = new TableColumn<>(getContext().getLabels().getString("position"));
        parameterNameColumn = new TableColumn<>(getContext().getLabels().getString("name"));
        parameterValueColumn = new TableColumn<>(getContext().getLabels().getString("value"));
        parameterUnitColumn = new TableColumn<>(getContext().getLabels().getString("unit"));
        descriptionColumn = new TableColumn<>(getContext().getLabels().getString("description"));

        saveButton.setText(getContext().getLabels().getString("save"));

        parameterTable.setEditable(true);

        seqColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        seqColumn.setOnEditCommit((TableColumn.CellEditEvent<MaterialParameters, Integer> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setSeq(t.getNewValue()));
        seqColumn.setCellValueFactory(cellData -> cellData.getValue().seqProperty().asObject());
        seqColumn.setSortType(TableColumn.SortType.ASCENDING);
        parameterTable.getColumns().add(seqColumn);
        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterName().nameProperty(getContext().getLanguage()));
        parameterTable.getColumns().add(parameterNameColumn);
        parameterValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        parameterValueColumn.setOnEditCommit((TableColumn.CellEditEvent<MaterialParameters, BigDecimal> t) -> t.getTableView().getItems().get(
                t.getTablePosition().getRow()).setVal(t.getNewValue()));
        parameterValueColumn.setCellValueFactory(cellData -> cellData.getValue().valProperty());
        parameterTable.getColumns().add(parameterValueColumn);
        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterUnit().getUnitName().nameProperty(getContext().getLanguage()));
        parameterTable.getColumns().add(parameterUnitColumn);
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().descriptionProperty());
        parameterTable.getColumns().add(descriptionColumn);

        parameterTable.setItems(this.materialParameters);

        errorLabel.setText("");

        materialName.setDisable(true);
        materialNumber.setDisable(true);
        description.setDisable(true);

/*        getContext().getSelection(Materials.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Materials material = (Materials) newValue;
                this.setCurrentEntity(material);
                materialName.textProperty().set(material.getMaterialName().getName(getContext().getLanguage()));
                materialNumber.textProperty().set(material.getMaterialNumber());
                description.textProperty().set(material.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                this.materialParameters.clear();
                List<MaterialParameters> parameters = getContext().getMaterialParameter(material);
                if (!parameters.isEmpty()) {
                    for (MaterialParameters materialParameter : parameters) {
                        if (!this.materialParameters.contains(materialParameter)) {
                            this.materialParameters.add(materialParameter);
                        }
                    }
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                }
                parameterTable.refresh();
            }
        });*/
        getContext().getSelection(Parameters.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null && getCurrentEntity() != null) {
                Parameters parameter = (Parameters) newValue;
                if (!isOnTheList(parameter)) {
                    MaterialParameters materialParameter = new MaterialParameters();
                    materialParameter.setSeq(getMaxSeq() + 1);
                    materialParameter.setMaterial((Materials) getCurrentEntity());
                    materialParameter.setParameter(parameter);
                    materialParameter.setVal(new BigDecimal("0.0"));
                    this.materialParameters.add(materialParameter);
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                } else {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText(getContext().getLabels().getString("objectAlreadyOnList"));
                }
            } else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            }
        });
        clearButton.setOnAction(event -> {
            this.setCurrentEntity(null);
            this.materialParameters.clear();
            materialName.textProperty().set("");
            description.textProperty().set("");
            materialNumber.textProperty().set("");
            parameterTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });
/*        saveButton.setOnAction(event -> {
            if (materialParameters.isEmpty() || getCurrentEntity() == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> result = getContext().saveList(materialParameters.stream().collect(Collectors.toList()));
                if (!result.isEmpty()) {
                    materialParameters.clear();
                    for (DBEntities entity : result) {
                        materialParameters.add((MaterialParameters) entity);
                    }
                    parameterTable.refresh();
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("listSaved"));
                }
            }
        });*/
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
        parameterTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                MaterialParameters materialParameter = parameterTable.getSelectionModel().getSelectedItem();
                if (materialParameter != null) {
                    if (getContext().deleteEntity(materialParameter)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    this.materialParameters.remove(materialParameter);
                    parameterTable.refresh();
                }
            }
        });
    }

    private boolean isOnTheList(Parameters parameter) {
        for (MaterialParameters materialParameter : materialParameters) {
            if (materialParameter.getParameter().equals(parameter)) return true;
        }
        return false;
    }

    private int getMaxSeq() {
        int result = 0;
        for (MaterialParameters mp : materialParameters) {
            if (mp.getSeq() > result) {
                result = mp.getSeq();
            }
        }
        return result;
    }
}
