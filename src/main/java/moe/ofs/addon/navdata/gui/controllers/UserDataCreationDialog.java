package moe.ofs.addon.navdata.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.services.UserDataService;
import moe.ofs.backend.object.map.GeoPosition;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class UserDataCreationDialog implements Initializable {

    private Stage stage;

    @FXML
    private DialogPane userDataCreationPane;

    @FXML
    private TextField identificationTextField;

    @FXML
    private TextField fixNameTextField;

    @FXML
    private TextField latitudeTextField;

    @FXML
    private TextField longitudeTextField;

    @FXML
    private TextField altitudeTextField;

    @FXML
    private TextField fixPointDescriptionTextField;

    @FXML
    private RadioButton dataTypeNavaidRadioButton;

    @FXML
    private RadioButton dataTypeWaypointRadioButton;

    @FXML
    private Button navFixUserDataSubmitButton;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private NavFix data;

    private final UserDataService<NavFix> userDataService;
    private final UserDataTitledPane userDataTitledPane;

    public UserDataCreationDialog(UserDataService<NavFix> userDataService, UserDataTitledPane userDataTitledPane) {
        this.userDataService = userDataService;
        this.userDataTitledPane = userDataTitledPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init user data form dialog");
        stage = new Stage();
        Scene scene = new Scene(userDataCreationPane);

        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);

        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);

        toggleGroup.getToggles().addAll(dataTypeNavaidRadioButton, dataTypeWaypointRadioButton);
    }

    public void show() {
        stage.show();
    }

    public Stage loadData(NavFix fix) {
        data = fix;
        identificationTextField.setText(data.getCode());
        latitudeTextField.setText(String.valueOf(data.getPosition().getLatitude()));
        longitudeTextField.setText(String.valueOf(data.getPosition().getLongitude()));

        if(fix instanceof Navaid) {
            altitudeTextField.setText(String.valueOf(data.getPosition().getAltitude()));
            fixNameTextField.setText(((Navaid) data).getName());

            toggleGroup.selectToggle(dataTypeNavaidRadioButton);
        } else {
            toggleGroup.selectToggle(dataTypeWaypointRadioButton);
        }

        return stage;
    }

    @FXML
    private void submitData() {
        long loadedDataId = -1;
        if(data != null) {
            loadedDataId = data.getId();
        }

        if(toggleGroup.getSelectedToggle().equals(dataTypeNavaidRadioButton)) {
            GeoPosition geoPosition = new GeoPosition();
            geoPosition.setLatitude(Double.parseDouble(latitudeTextField.getText()));
            geoPosition.setLongitude(Double.parseDouble(longitudeTextField.getText()));
            geoPosition.setAltitude(Double.parseDouble(altitudeTextField.getText()));

            // need a converter here to convert LL(different format)/LO
            data = Navaid.builder()
                    .code(identificationTextField.getText())
                    .name(fixNameTextField.getText())
                    .position(geoPosition)
                    .build();

        } else {
            GeoPosition geoPosition = new GeoPosition();
            geoPosition.setLatitude(Double.parseDouble(latitudeTextField.getText()));
            geoPosition.setLongitude(Double.parseDouble(longitudeTextField.getText()));

            data = Waypoint.builder()
                    .code(identificationTextField.getText())
                    .position(geoPosition)
                    .build();
        }

        stage.close();

        if(loadedDataId > -1) {
            data.setId(loadedDataId);
            userDataTitledPane.updateInDataListView(data);
        } else {
            userDataTitledPane.addToUserDataListView(data);
        }

        userDataService.save(data);
        data = null;

        System.out.println("userDataService = " + userDataService.findAll());
    }

}
