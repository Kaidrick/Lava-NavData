package moe.ofs.addon.navdata.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.services.WaypointService;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView
public class WaypointTitledPane implements Initializable, Searchable {

    private final WaypointService service;

    @FXML
    private ListView<Waypoint> waypointListView;

    @FXML
    private TextField waypointSearchTextField;

    public WaypointTitledPane(WaypointService service) {
        this.service = service;
    }

    public void addToListView(Waypoint waypoint) {
        waypointListView.getItems().add(waypoint);
    }

    public void clearListView() {
        waypointListView.getItems().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        waypointSearchTextField.textProperty()
                .addListener(((observable, oldValue, newValue) -> search(waypointListView, newValue, service)));
    }
}
