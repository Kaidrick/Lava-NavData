package moe.ofs.addon.navdata.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import moe.ofs.addon.navdata.services.ReferencePointService;
import moe.ofs.backend.function.ReferencePointManager;
import moe.ofs.backend.object.map.ReferencePoint;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView
public class ReferencePointDataTitledPane implements Initializable {

    private final ReferencePointService referencePointService;
    private final ReferencePointManager referencePointManager;

    public ReferencePointDataTitledPane(ReferencePointService referencePointService, ReferencePointManager referencePointManager) {
        this.referencePointService = referencePointService;
        this.referencePointManager = referencePointManager;
    }

    @FXML
    private Label coalitionRedLabel;

    @FXML
    private Label coalitionBlueLabel;

    @FXML
    private ListView<ReferencePoint> blueRefPointListView;

    @FXML
    private ListView<ReferencePoint> redRefPointListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        referencePointManager.getAll().forEach(referencePointService::save);
        referencePointService.findAll().forEach(blueRefPointListView.getItems()::add);
    }


    // add a few buttons here


}
