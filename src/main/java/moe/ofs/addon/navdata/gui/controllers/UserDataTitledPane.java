package moe.ofs.addon.navdata.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ListView;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.services.UserDataService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class UserDataTitledPane implements Initializable {

    private final UserDataService<NavFix> userDataService;

    private final FxWeaver fxWeaver;

    @FXML
    private ListView<NavFix> navFixUserDataListView;

    public UserDataTitledPane(UserDataService<NavFix> userDataService, FxWeaver fxWeaver) {
        this.userDataService = userDataService;
        this.fxWeaver = fxWeaver;
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
    }

    public void updateInDataListView(NavFix fix) {
        int index = navFixUserDataListView.getSelectionModel().getSelectedIndex();
        navFixUserDataListView.getItems().remove(index);
        navFixUserDataListView.getItems().add(index, fix);
    }

    public void removeFromUserDataListView(NavFix fix) {
        navFixUserDataListView.getItems().remove(fix);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
