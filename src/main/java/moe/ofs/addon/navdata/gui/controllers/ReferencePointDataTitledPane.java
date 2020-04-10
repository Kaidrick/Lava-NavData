package moe.ofs.addon.navdata.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import moe.ofs.addon.navdata.services.ReferencePointService;
import moe.ofs.backend.object.map.ReferencePoint;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView
public class ReferencePointDataTitledPane implements Initializable {

    private boolean initialized;

    private final ReferencePointService referencePointService;

    public ReferencePointDataTitledPane(ReferencePointService referencePointService) {
        this.referencePointService = referencePointService;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @FXML
    private Label coalitionRedLabel;

    @FXML
    private Label coalitionBlueLabel;

    @FXML
    private ListView<ReferencePoint> blueRefPointListView;

    @FXML
    private ListView<ReferencePoint> redRefPointListView;

    public void addToBlueListView(ReferencePoint point) {
        blueRefPointListView.getItems().add(point);
    }

    public void addToRedListView(ReferencePoint point) {
        redRefPointListView.getItems().add(point);
    }

    public void clearListViews() {
        blueRefPointListView.getItems().clear();
        redRefPointListView.getItems().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        referencePointService.findAll().forEach(blueRefPointListView.getItems()::add);
        initialized = true;
    }
}
