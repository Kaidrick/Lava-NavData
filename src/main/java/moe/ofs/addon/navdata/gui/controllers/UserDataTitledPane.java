package moe.ofs.addon.navdata.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class UserDataTitledPane implements Initializable {

    @FXML
    private void testButtonFunction(ActionEvent actionEvent) {
        System.out.println("button pressed " + getClass().getCanonicalName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
