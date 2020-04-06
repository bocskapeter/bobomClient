package eu.bopet.bobom.gui.controllers;

import eu.bopet.bobom.core.entities.*;
import eu.bopet.bobom.core.entities.names.*;
import eu.bopet.bobom.gui.GUIContext;
import eu.bopet.bobom.gui.controllers.search.*;
import eu.bopet.bobom.gui.controllers.search.names.*;
import eu.bopet.bobom.gui.controllers.work.*;
import eu.bopet.bobom.gui.controllers.work.name.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EngineeringController implements Initializable {

    private final List<GUIController> searchControllers;
    private final List<GUIController> workControllers;
    private final GUIContext context;

    @FXML
    private Label searchInLabel;
    @FXML
    private ChoiceBox<GUIController> searchInList;
    @FXML
    private Label workOnLabel;
    @FXML
    private ChoiceBox<GUIController> workOnList;
    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private Label statusLabel;


    public EngineeringController(GUIContext context) {
        this.searchControllers = new ArrayList<>();
        this.workControllers = new ArrayList<>();
        this.context = context;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchInLabel.setText(context.getLabels().getString("searchIn"));
        workOnLabel.setText(context.getLabels().getString("workOn"));
        context.registerEntityClass(Boms.class);
        context.registerEntityClass(CategoryGroupNames.class);
        context.registerEntityClass(CategoryGroups.class);
        context.registerEntityClass(CategoryNames.class);
        context.registerEntityClass(CategoryParameters.class);
        context.registerEntityClass(Categories.class);
        context.registerEntityClass(ItemCategories.class);
        context.registerEntityClass(ItemMaterials.class);
        context.registerEntityClass(ItemNames.class);
        context.registerEntityClass(Items.class);
        context.registerEntityClass(ParameterValues.class);
        context.registerEntityClass(ItemStandards.class);
        context.registerEntityClass(MaterialNames.class);
        context.registerEntityClass(Materials.class);
        context.registerEntityClass(MaterialParameters.class);
        context.registerEntityClass(MaterialStandards.class);
        context.registerEntityClass(ParameterNames.class);
        context.registerEntityClass(Parameters.class);
        context.registerEntityClass(Quantities.class);
        context.registerEntityClass(QuantityNames.class);
        context.registerEntityClass(StandardNames.class);
        context.registerEntityClass(Standards.class);
        context.registerEntityClass(UnitNames.class);
        context.registerEntityClass(Units.class);

        registerNameWorkController(CategoryGroupNameController.class);
        registerNameWorkController(CategoryNameController.class);
        registerNameWorkController(ItemNameController.class);
        registerNameWorkController(MaterialNameController.class);
        registerNameWorkController(ParameterNameController.class);
        registerNameWorkController(QuantityNameController.class);
        registerNameWorkController(StandardNameController.class);
        registerNameWorkController(UnitNameController.class);

        registerNamesSearchController(CategoryGroupNamesController.class);
        registerNamesSearchController(CategoryNamesController.class);
        registerNamesSearchController(ItemNamesController.class);
        registerNamesSearchController(MaterialNamesController.class);
        registerNamesSearchController(ParameterNamesController.class);
        registerNamesSearchController(QuantityNamesController.class);
        registerNamesSearchController(StandardNamesController.class);
        registerNamesSearchController(UnitNamesController.class);

        BomsSearchController bomsSearchController = new BomsSearchController(context);
        loadFXMLController("/fxml/search/SearchBoms.fxml", bomsSearchController, searchControllers);

        CategoriesSearchController categoriesSearchController = new CategoriesSearchController(context);
        loadFXMLController("/fxml/search/SearchCategories.fxml", categoriesSearchController, searchControllers);

        CategoryGroupsSearchController categoryGroupsSearchController = new CategoryGroupsSearchController(context);
        loadFXMLController("/fxml/search/SearchCategoryGroups.fxml", categoryGroupsSearchController, searchControllers);

        ItemsSearchController itemsSearchController = new ItemsSearchController(context);
        loadFXMLController("/fxml/search/SearchItems.fxml", itemsSearchController, searchControllers);

        MaterialsSearchController materialsSearchController = new MaterialsSearchController(context);
        loadFXMLController("/fxml/search/SearchMaterials.fxml", materialsSearchController, searchControllers);

        ParametersSearchController parametersSearchController = new ParametersSearchController(context);
        loadFXMLController("/fxml/search/SearchParameters.fxml", parametersSearchController, searchControllers);

        QuantitiesSearchController quantitiesSearchController = new QuantitiesSearchController(context);
        loadFXMLController("/fxml/search/SearchQuantities.fxml", quantitiesSearchController, searchControllers);

        StandardsSearchController standardsSearchController = new StandardsSearchController(context);
        loadFXMLController("/fxml/search/SearchStandards.fxml", standardsSearchController, searchControllers);

        UnitsSearchController unitsSearchController = new UnitsSearchController(context);
        loadFXMLController("/fxml/search/SearchUnits.fxml", unitsSearchController, searchControllers);

        BomWorkController bomWorkController = new BomWorkController(context);
        loadFXMLController("/fxml/work/WorkBom.fxml", bomWorkController, workControllers);

        CategoryWorkController categoryWorkController = new CategoryWorkController(context);
        loadFXMLController("/fxml/work/WorkCategory.fxml", categoryWorkController, workControllers);

        CategoryGroupWorkController categoryGroupWorkController = new CategoryGroupWorkController(context);
        loadFXMLController("/fxml/work/WorkCategoryGroup.fxml", categoryGroupWorkController, workControllers);

        CategoryParameterWorkController categoryParameterWorkController = new CategoryParameterWorkController(context);
        loadFXMLController("/fxml/work/WorkCategoryParameter.fxml", categoryParameterWorkController, workControllers);

        ItemWorkController itemWorkController = new ItemWorkController(context);
        loadFXMLController("/fxml/work/WorkItem.fxml", itemWorkController, workControllers);

        ItemCategoryWorkController itemCategoryWorkController = new ItemCategoryWorkController(context);
        loadFXMLController("/fxml/work/WorkItemCategory.fxml", itemCategoryWorkController, workControllers);

        ItemMaterialWorkController itemMaterialWorkController = new ItemMaterialWorkController(context);
        loadFXMLController("/fxml/work/WorkItemMaterial.fxml", itemMaterialWorkController, workControllers);

        ItemParameterWorkController itemParameterWorkController = new ItemParameterWorkController(context);
        loadFXMLController("/fxml/work/WorkItemParameter.fxml", itemParameterWorkController, workControllers);

        ItemStandardWorkController itemStandardWorkController = new ItemStandardWorkController(context);
        loadFXMLController("/fxml/work/WorkItemStandard.fxml", itemStandardWorkController, workControllers);

        MaterialWorkController materialWorkController = new MaterialWorkController(context);
        loadFXMLController("/fxml/work/WorkMaterial.fxml", materialWorkController, workControllers);

        MaterialParameterWorkController materialParameterWorkController = new MaterialParameterWorkController(context);
        loadFXMLController("/fxml/work/WorkMaterialParameter.fxml", materialParameterWorkController, workControllers);

        MaterialStandardWorkController materialStandardWorkController = new MaterialStandardWorkController(context);
        loadFXMLController("/fxml/work/WorkMaterialStandard.fxml", materialStandardWorkController, workControllers);

        ParameterWorkController parameterWorkController = new ParameterWorkController(context);
        loadFXMLController("/fxml/work/WorkParameter.fxml", parameterWorkController, workControllers);

        QuantityWorkController quantityWorkController = new QuantityWorkController(context);
        loadFXMLController("/fxml/work/WorkQuantity.fxml", quantityWorkController, workControllers);

        StandardWorkController standardWorkController = new StandardWorkController(context);
        loadFXMLController("/fxml/work/WorkStandard.fxml", standardWorkController, workControllers);

        UnitWorkController unitWorkController = new UnitWorkController(context);
        loadFXMLController("/fxml/work/WorkUnit.fxml", unitWorkController, workControllers);

        ObservableList<GUIController> list = FXCollections.observableArrayList(searchControllers);
        searchInList.setItems(list);
        searchInList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                context.getLabels().getString("waitLoadingApp");
                context.loadAllData(newValue.getEntityClass());
                leftPane.getChildren().clear();
                leftPane.getChildren().add(newValue.getPane());
                context.getLabels().getString("ready");
            }
        });

        ObservableList<GUIController> workList = FXCollections.observableArrayList(workControllers);
        workOnList.setItems(workList);
        workOnList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                context.getLabels().getString("waitLoadingApp");
                context.loadAllData(newValue.getEntityClass());
                rightPane.getChildren().clear();
                rightPane.getChildren().add(newValue.getPane());
                context.getLabels().getString("ready");
            }
        });
        statusLabel.setText(context.getLabels().getString("ready"));
    }

    private <T> void registerNameWorkController(Class<T> nameWorkControllerClass) {
        try {
            Constructor<T> cons = nameWorkControllerClass.getDeclaredConstructor(GUIContext.class);
            T nameController = cons.newInstance(context);
            loadFXMLController("/fxml/work/WorkName.fxml", (GUIController) nameController, workControllers);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private <T> void registerNamesSearchController(Class<T> nameSearchControllerClass) {
        try {
            Constructor<T> cons = nameSearchControllerClass.getDeclaredConstructor(GUIContext.class);
            T nameController = cons.newInstance(context);
            loadFXMLController("/fxml/search/SearchNames.fxml", (GUIController) nameController, searchControllers);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void loadFXMLController(String name, GUIController controller, List<GUIController> controllers) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
            loader.setController(controller);
            BorderPane pane = loader.load();
            controller.setPane(pane);
            controllers.add(controller);
        } catch (IOException e) {
            Logger.getLogger(EngineeringController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
