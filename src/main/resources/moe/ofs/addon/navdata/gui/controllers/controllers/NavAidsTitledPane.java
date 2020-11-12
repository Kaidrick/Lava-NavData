//package moe.ofs.addon.navdata.gui.controllers;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import moe.ofs.addon.navdata.domain.Navaid;
//import moe.ofs.addon.navdata.services.NavaidService;
//import net.rgielen.fxweaver.core.FxmlView;
//import org.springframework.stereotype.Controller;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//@Controller
//@FxmlView
//public class NavAidsTitledPane implements Initializable, Searchable {
//
//    private boolean initialized;
//
//    private final NavaidService service;
//
//    public NavAidsTitledPane(NavaidService service) {
//        this.service = service;
//    }
//
//    @FXML
//    private ListView<Navaid> navaidListView;
//
//    @FXML
//    private TextField navaidSearchTextField;
//
//    public void addToListView(Navaid navaid) {
//        navaidListView.getItems().add(navaid);
//    }
//
//    public void clearListView() {
//        navaidListView.getItems().clear();
//    }
//
//    public boolean isInitialized() {
//        return initialized;
//    }
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        service.findAll().forEach(this::addToListView);
//
//        navaidSearchTextField.textProperty()
//                .addListener(((observable, oldValue, newValue) -> search(navaidListView, newValue, service)));
//
//        initialized = true;
//    }
//}
