package moe.ofs.addon.navdata.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import moe.ofs.addon.navdata.NavDataProvider;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.services.UserDataService;
import moe.ofs.backend.domain.BaseEntity;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView
public class UserDataTitledPane implements Initializable {

    private final FxWeaver fxWeaver;

    private final NavDataProvider navDataProvider;

    private final UserDataService<NavFix> userDataService;

    @FXML
    private ListView<NavFix> navFixUserDataListView;

    public UserDataTitledPane(FxWeaver fxWeaver, NavDataProvider navDataProvider, UserDataService<NavFix> userDataService) {
        this.fxWeaver = fxWeaver;
        this.navDataProvider = navDataProvider;
        this.userDataService = userDataService;
    }

    @FXML
    private void addUserData(ActionEvent actionEvent) {
        // make controller responsible for opening new stage
        fxWeaver.loadController(UserDataCreationDialog.class).show();
    }

    @FXML
    private void removeUserData(ActionEvent actionEvent) {
        removeFromUserDataListView(navFixUserDataListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editUserData(ActionEvent actionEvent) {
        NavFix fix = navFixUserDataListView.getSelectionModel().getSelectedItem();
        if (fix != null)
            fxWeaver.loadController(UserDataCreationDialog.class).loadData(fix).show();
    }

    public void addToUserDataListView(NavFix fix) {
        navFixUserDataListView.getItems().add(fix);

        navDataProvider.writeFile(new ArrayList<>(navFixUserDataListView.getItems()),
                "user_nav_fix");
    }

    public void updateInDataListView(NavFix fix) {
        int index = navFixUserDataListView.getSelectionModel().getSelectedIndex();
        navFixUserDataListView.getItems().remove(index);
        navFixUserDataListView.getItems().add(index, fix);

        navDataProvider.writeFile(new ArrayList<>(navFixUserDataListView.getItems()),
                "user_nav_fix");
    }

    public void removeFromUserDataListView(NavFix fix) {
        navFixUserDataListView.getItems().remove(fix);
        userDataService.deleteById(fix.getId());

        System.out.println("userDataService.findAll() = " + userDataService.findAll());
        userDataService.findAll().stream()
                .map(BaseEntity::getId)
                .forEach(System.out::println);

        navDataProvider.writeFile(new ArrayList<>(navFixUserDataListView.getItems()),
                "user_nav_fix");
    }

    @FXML
    private void getReferencePoints() {
        //
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(navDataProvider.dataFileExists("user_nav_fix")) {
            List<NavFix> navFixList = navDataProvider.readFile("user_nav_fix");
            navFixList.forEach(navFixUserDataListView.getItems()::add);
            navFixList.forEach(userDataService::save);

            System.out.println("userDataService.findAll() = " + userDataService.findAll());
        }
    }
}
