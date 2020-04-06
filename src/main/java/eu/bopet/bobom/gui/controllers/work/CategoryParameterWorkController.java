package eu.bopet.bobom.gui.controllers.work;

import eu.bopet.bobom.core.entities.Categories;
import eu.bopet.bobom.core.entities.CategoryParameters;
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
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryParameterWorkController extends GUIController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField categoryName;
    @FXML
    private Label categoryGroupLabel;
    @FXML
    private TextField categoryGroup;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField description;
    @FXML
    private Label parametersLabel;
    @FXML
    private TableView<CategoryParameters> parameterTable;
    private TableColumn<CategoryParameters, Integer> seqColumn;
    private TableColumn<CategoryParameters, String> parameterNameColumn;
    private TableColumn<CategoryParameters, String> parameterUnitColumn;
    private TableColumn<CategoryParameters, String> descriptionColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;

    private Categories currentCategory;
    private ObservableList<CategoryParameters> categoryParameters;

    public CategoryParameterWorkController(GUIContext context) {
        super(context.getLabels().getString("categoryParameter"), CategoryParameters.class, context);
        List<CategoryParameters> categoryParametersArrayList = new ArrayList<>();
        categoryParameters = FXCollections.observableArrayList(categoryParametersArrayList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(getContext().getLabels().getString("categoryParameter"));
        secondTitleLabel.setText(getContext().getLabels().getString("createNewOrDelete"));
        clearButton.setText(getContext().getLabels().getString("clear"));

        nameLabel.setText(getContext().getLabels().getString("name") + Utils.REQUIRED_FIELD);
        categoryGroupLabel.setText(getContext().getLabels().getString("categoryGroup") + Utils.REQUIRED_FIELD);
        descriptionLabel.setText(getContext().getLabels().getString("description") + Utils.REQUIRED_FIELD);
        parametersLabel.setText(getContext().getLabels().getString("parameters") + Utils.REQUIRED_FIELD);

        seqColumn = new TableColumn<>(getContext().getLabels().getString("position"));
        parameterNameColumn = new TableColumn<>(getContext().getLabels().getString("name"));
        parameterUnitColumn = new TableColumn<>(getContext().getLabels().getString("unit"));
        descriptionColumn = new TableColumn<>(getContext().getLabels().getString("description"));

        saveButton.setText(getContext().getLabels().getString("save"));
        categoryName.setDisable(true);
        categoryGroup.setDisable(true);
        description.setDisable(true);

        clearButton.setOnAction(event -> {
            categoryName.setText("");
            categoryGroup.setText("");
            description.setText("");
            currentCategory = null;
            categoryParameters.clear();
            parameterTable.refresh();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(getContext().getLabels().getString("fieldsAreCleared"));
        });

        parameterTable.getColumns().clear();
        parameterTable.setItems(categoryParameters);
        parameterTable.setEditable(true);

        seqColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        seqColumn.setOnEditCommit((TableColumn.CellEditEvent<CategoryParameters, Integer> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setSeq(t.getNewValue()));
        seqColumn.setCellValueFactory(cellData -> cellData.getValue().seqProperty().asObject());
        seqColumn.setSortType(TableColumn.SortType.ASCENDING);
        parameterTable.getColumns().add(seqColumn);
        parameterNameColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterName().nameProperty(getContext().getLanguage()));
        parameterTable.getColumns().add(parameterNameColumn);
        parameterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().getParameterUnit().getUnitName().nameProperty(getContext().getLanguage()));
        parameterTable.getColumns().add(parameterUnitColumn);
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getParameter().descriptionProperty());
        parameterTable.getColumns().add(descriptionColumn);

        errorLabel.setText("");

        parameterTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                CategoryParameters categoryParameter = parameterTable.getSelectionModel().getSelectedItem();
                if (categoryParameter != null) {
                    if (getContext().deleteEntity(categoryParameter)) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText(getContext().getLabels().getString("successfullyDeleted"));
                    }
                    parameterTable.getItems().remove(categoryParameter);
                    parameterTable.refresh();
                }
            }
        });

        getContext().getSelection(Categories.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Categories category = (Categories) newValue;
                currentCategory = category;
                categoryName.textProperty().set(category.getCategoryName().getName(getContext().getLanguage()));
                categoryGroup.textProperty().set(category.getCategoryGroup().getCategoryGroupName().getName(getContext().getLanguage()));
                description.textProperty().set(category.getDescription());
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                categoryParameters.clear();
                List<CategoryParameters> parameters = getContext().getCategoryParameters(category);
                if (!parameters.isEmpty()) {
                    categoryParameters.addAll(parameters);
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
                }
                categoryParameters.remove(parameters);
                parameterTable.getSortOrder().add(seqColumn);
                parameterTable.refresh();
            }
        });

        getContext().getSelection(Parameters.class).addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CategoryParameters categoryParameter = new CategoryParameters();
                categoryParameter.setSeq(getMaxSeq() + 1);
                categoryParameter.setCategory(currentCategory);
                categoryParameter.setParameter((Parameters) newValue);
                categoryParameters.add(categoryParameter);
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText(getContext().getLabels().getString("fieldsAreCopied"));
            }
        });

/*        saveButton.setOnAction(event -> {
            if (currentCategory == null || categoryParameters.isEmpty()) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText(getContext().getLabels().getString("fillRequiredFields"));
            } else {
                List<DBEntities> result = getContext().saveList(categoryParameters.stream().collect(Collectors.toList()));
                if (!result.isEmpty()) {
                    categoryParameters.clear();
                    for (DBEntities e : result) {
                        categoryParameters.add((CategoryParameters) e);
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
    }

    private int getMaxSeq() {
        int result = 0;
        for (CategoryParameters c : categoryParameters) {
            if (c.getSeq() > result) {
                result = c.getSeq();
            }
        }
        return result;
    }
}
