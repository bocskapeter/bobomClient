package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.MaterialStandards;
import eu.bopet.bobom.core.entities.Materials;
import eu.bopet.bobom.core.entities.Standards;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.Utils;
import eu.bopet.bobom.gui.controllers.GUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MaterialStandardWorkController extends GUIController implements Initializable {

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
    private Label materialStandardLabel;
    @FXML
    private TableView<MaterialStandards> standardTable;
    @FXML
    private TableColumn<MaterialStandards, String> standardNumberColumn;
    @FXML
    private TableColumn<MaterialStandards, String> standardNameColumn;
    @FXML
    private TableColumn<MaterialStandards, String> descriptionColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    private ObservableList<MaterialStandards> materialStandards;

    public MaterialStandardWorkController(GUIContext context) {
        super(context.getLabels().getString("materialStandard"), MaterialStandards.class, context);
        List<MaterialStandards> materialStandards = new ArrayList<>();
        this.materialStandards = FXCollections.observableArrayList(materialStandards);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("materialStandard"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        numberLabel.setText(getContext().getLabels().getString("number") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);

        materialStandardLabel.setText(getContext().getLabels().getString("standard"));
        standardNumberColumn.setText(getContext().getLabels().getString("number"));
        standardNameColumn.setText(getContext().getLabels().getString("name"));
        descriptionColumn.setText(getContext().getLabels().getString("description"));

        saveButton.setText(getContext().getLabels().getString("save"));

        standardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().standardNumberProperty());
        standardNameColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().getStandardName().nameProperty(getContext().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getStandard().descriptionProperty());

        standardTable.setItems(this.materialStandards);

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
                this.materialStandards.clear();
                List<MaterialStandards> standards = getContext().getMaterialStandards(material);
                if (!standards.isEmpty()) {
                    for (MaterialStandards materialStandard : standards) {
                        if (!materialStandards.contains(materialStandard)) {
                            materialStandards.add(materialStandard);
                        }
                    }
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                }
                standardTable.refresh();
            }
        });*/
        getContext().getSelection(Standards.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null && getCurrentEntity() != null) {
                Standards standard = (Standards) newValue;
                if (!isOnTheList(standard)) {
                    MaterialStandards materialStandard = new MaterialStandards();
                    materialStandard.setMaterial((Materials) getCurrentEntity());
                    materialStandard.setStandard(standard);
                    materialStandards.add(materialStandard);
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
            this.materialStandards.clear();
            materialName.textProperty().set("");
            description.textProperty().set("");
            materialNumber.textProperty().set("");
            standardTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });
/*        saveButton.setOnAction(event -> {
            if (materialStandards.isEmpty() || getCurrentEntity() == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> result = getContext().saveList(materialStandards.stream().collect(Collectors.toList()));
                if (!result.isEmpty()) {
                    materialStandards.clear();
                    for (DBEntities entity : result) {
                        materialStandards.add((MaterialStandards) entity);
                    }
                    standardTable.refresh();
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("listSaved"));
                }
            }
        });*/
        errorLabel.setOnMouseClicked(event -> {
            getContext().putToClipboard(errorLabel.getText());
        });
        standardTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                MaterialStandards materialStandard = standardTable.getSelectionModel().getSelectedItem();
                if (materialStandard != null) {
                    if (getContext().deleteEntity(materialStandard)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    materialStandards.remove(materialStandard);
                    standardTable.refresh();
                }
            }
        });
    }

    private boolean isOnTheList(Standards standard) {
        for (MaterialStandards materialStandard : materialStandards) {
            if (materialStandard.getStandard().equals(standard)) return true;
        }
        return false;
    }
}
