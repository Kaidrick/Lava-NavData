//package moe.ofs.addon.navdata.gui.controllers;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.control.ListView;
//import javafx.stage.Stage;
//import moe.ofs.addon.navdata.NavDataProvider;
//import moe.ofs.addon.navdata.domain.NavFix;
//import moe.ofs.addon.navdata.services.UserDataService;
//import moe.ofs.backend.domain.BaseEntity;
//import moe.ofs.backend.interaction.StageControl;
//import net.rgielen.fxweaver.core.FxWeaver;
//import net.rgielen.fxweaver.core.FxmlView;
//import org.springframework.stereotype.Controller;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.ResourceBundle;
//
//@Controller
//@FxmlView
//public class UserDataTitledPane implements Initializable {
//
//    private final FxWeaver fxWeaver;
//
//    private final NavDataProvider navDataProvider;
//
//    private final UserDataService<NavFix> userDataService;
//
//    @FXML
//    private ListView<NavFix> navFixUserDataListView;
//
//    public UserDataTitledPane(FxWeaver fxWeaver, NavDataProvider navDataProvider, UserDataService<NavFix> userDataService) {
//        this.fxWeaver = fxWeaver;
//        this.navDataProvider = navDataProvider;
//        this.userDataService = userDataService;
//    }
//
//    @FXML
//    private void addUserData(ActionEvent actionEvent) {
//        Node source = (Node) actionEvent.getSource();
//        Stage parent  = (Stage) source.getScene().getWindow();
//
//        // make controller responsible for opening new stage
//        Stage stage = fxWeaver.loadController(UserDataCreationDialog.class).getStage();
//        StageControl.showOnParentCenter(stage, parent);
//    }
//
//    @FXML
//    private void removeUserData(ActionEvent actionEvent) {
//        removeFromUserDataListView(navFixUserDataListView.getSelectionModel().getSelectedItem());
//    }
//
//    @FXML
//    private void editUserData(ActionEvent actionEvent) {
//        Node source = (Node) actionEvent.getSource();
//        Stage parent  = (Stage) source.getScene().getWindow();
//
//        NavFix fix = navFixUserDataListView.getSelectionModel().getSelectedItem();
//        if (fix != null) {
//            Stage stage = fxWeaver.loadController(UserDataCreationDialog.class).loadData(fix);
//            StageControl.showOnParentCenter(stage, parent);
//        }
//    }
//
//    public void addToUserDataListView(NavFix fix) {
//        navFixUserDataListView.getItems().add(fix);
//
//        navDataProvider.writeFile(new ArrayList<>(navFixUserDataListView.getItems()),
//                "user_nav_fix");
//    }
//
//    public void updateInDataListView(NavFix fix) {
//        int index = navFixUserDataListView.getSelectionModel().getSelectedIndex();
//        navFixUserDataListView.getItems().remove(index);
//        navFixUserDataListView.getItems().add(index, fix);
//
//        navDataProvider.writeFile(new ArrayList<>(navFixUserDataListView.getItems()),
//                "user_nav_fix");
//    }
//
//    public void removeFromUserDataListView(NavFix fix) {
//        navFixUserDataListView.getItems().remove(fix);
//        userDataService.deleteById(fix.getId());
//
//        System.out.println("userDataService.findAll() = " + userDataService.findAll());
//        userDataService.findAll().stream()
//                .map(BaseEntity::getId)
//                .forEach(System.out::println);
//
//        navDataProvider.writeFile(new ArrayList<>(navFixUserDataListView.getItems()),
//                "user_nav_fix");
//    }
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        userDataService.findAll().forEach(navFixUserDataListView.getItems()::add);
//    }
//}
