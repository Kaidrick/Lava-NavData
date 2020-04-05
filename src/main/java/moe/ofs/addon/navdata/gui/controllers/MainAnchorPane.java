package moe.ofs.addon.navdata.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.services.NavaidService;
import moe.ofs.addon.navdata.services.WaypointService;
import moe.ofs.backend.UTF8Control;
import moe.ofs.backend.util.I18n;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class MainAnchorPane implements Initializable {

    @FXML
    private AnchorPane containerPane;

    @FXML
    private Accordion accordion;

    @FXML
    private ListView<Navaid> navaidListView;

    @FXML
    private ListView<Waypoint> waypointListView;
    // ListView<CustomData> dataListView;

    @FXML
    private AnchorPane userDataTitledPane;

    private final NavaidService navaidService;
    private final WaypointService waypointService;

    public MainAnchorPane(NavaidService navaidService, WaypointService waypointService) {
        this.navaidService = navaidService;
        this.waypointService = waypointService;
    }

    public <T extends NavFix> void addToListView(T data) {
        if(data instanceof Navaid) {
            navaidListView.getItems().add((Navaid) data);
        } else {
            waypointListView.getItems().add((Waypoint) data);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializing MainAnchorPane");

        accordion.setExpandedPane(accordion.getPanes().get(2));
        accordion.expandedPaneProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null)
                System.out.println("expended pane is " + newValue.getText());
        }));

        navaidListView.getItems().clear();
        navaidListView.getItems().addAll(navaidService.findAll());

        waypointListView.getItems().clear();
        waypointListView.getItems().addAll(waypointService.findAll());

        // listen to service add or delete operation

        I18n.localeProperty().addListener(((observable, oldValue, newValue) -> {
            ResourceBundle bundle =
                    ResourceBundle.getBundle("NavData", newValue, new UTF8Control());

            I18n.toPaneOrNotToPane(containerPane, bundle);
        }));
    }
}
