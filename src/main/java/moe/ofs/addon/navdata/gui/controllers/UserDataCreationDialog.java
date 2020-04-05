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
import moe.ofs.backend.GeoPositions;
import moe.ofs.backend.object.map.GeoPosition;
import moe.ofs.backend.object.map.Orientation;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class UserDataCreationDialog implements Initializable {

    private Stage stage;

    @FXML private DialogPane userDataCreationPane;
    @FXML private TextField identificationTextField;
    @FXML private TextField fixNameTextField;

    @FXML private TextField latitudeDegreeTextField;
    @FXML private TextField latitudeMinuteTextField;
    @FXML private TextField latitudeSecondTextField;

    @FXML private TextField longitudeDegreeTextField;
    @FXML private TextField longitudeMinuteTextField;
    @FXML private TextField longitudeSecondTextField;

    @FXML private TextField altitudeTextField;

    @FXML private TextField fixPointDescriptionTextField;

    @FXML private RadioButton dataTypeNavaidRadioButton;
    @FXML private RadioButton dataTypeWaypointRadioButton;
    @FXML private RadioButton dataFormatStandardRadioButton;
    @FXML private RadioButton dataFormatPrecisionRadioButton;

    @FXML private ComboBox<Orientation> latitudeOrientComboBox;
    @FXML private ComboBox<Orientation> longitudeOrientComboBox;


    @FXML private Button navFixUserDataSubmitButton;

    private ToggleGroup dataTypeToggleGroup = new ToggleGroup();
    private ToggleGroup dataFormatToggleGroup = new ToggleGroup();

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

        dataTypeToggleGroup.getToggles().addAll(dataTypeNavaidRadioButton, dataTypeWaypointRadioButton);
        dataFormatToggleGroup.getToggles().addAll(dataFormatStandardRadioButton, dataFormatPrecisionRadioButton);

        latitudeOrientComboBox.getItems().addAll(Orientation.NORTH, Orientation.SOUTH);
        longitudeOrientComboBox.getItems().addAll(Orientation.EAST, Orientation.WEST);
    }

    public void show() {
        stage.show();
    }

    public Stage loadData(NavFix fix) {
        data = fix;
        identificationTextField.setText(data.getCode());
        fixPointDescriptionTextField.setText(data.getDescription());

        String[] displays = GeoPositions.formatStringArray(data.getPosition(), false);

        if(displays[0].equals("N"))
            latitudeOrientComboBox.getSelectionModel().select(Orientation.NORTH);
        else
            latitudeOrientComboBox.getSelectionModel().select(Orientation.SOUTH);

        latitudeDegreeTextField.setText(displays[1]);
        latitudeMinuteTextField.setText(displays[2]);
        latitudeSecondTextField.setText(displays[3]);

        if(displays[4].equals("E"))
            longitudeOrientComboBox.getSelectionModel().select(Orientation.EAST);
        else
            longitudeOrientComboBox.getSelectionModel().select(Orientation.WEST);

        longitudeDegreeTextField.setText(displays[5]);
        longitudeMinuteTextField.setText(displays[6]);
        longitudeSecondTextField.setText(displays[7]);

        if(fix instanceof Navaid) {
            altitudeTextField.setText(String.valueOf(data.getPosition().getAltitude()));
            fixNameTextField.setText(((Navaid) data).getName());

            dataTypeToggleGroup.selectToggle(dataTypeNavaidRadioButton);
        } else {
            dataTypeToggleGroup.selectToggle(dataTypeWaypointRadioButton);
        }

        return stage;
    }

    @FXML
    private void submitData() {
        long loadedDataId = -1;
        if(data != null) {
            loadedDataId = data.getId();
        }

        // insert orientation
        GeoPosition geoPosition = GeoPositions.get(
                latitudeOrientComboBox.getValue(),
                latitudeDegreeTextField.getText(), latitudeMinuteTextField.getText(),
                latitudeSecondTextField.getText(),

                longitudeOrientComboBox.getValue(),
                longitudeDegreeTextField.getText(), longitudeMinuteTextField.getText(),
                longitudeSecondTextField.getText()
        );

        if(dataTypeToggleGroup.getSelectedToggle().equals(dataTypeNavaidRadioButton)) {
            geoPosition.setAltitude(Double.parseDouble(altitudeTextField.getText()));

            // need a converter here to convert LL(different format)/LO
            data = Navaid.builder()
                    .code(identificationTextField.getText())
                    .name(fixNameTextField.getText())
                    .position(geoPosition)
                    .description(fixPointDescriptionTextField.getText())
                    .build();

        } else {
            data = Waypoint.builder()
                    .code(identificationTextField.getText())
                    .position(geoPosition)
                    .description(fixPointDescriptionTextField.getText())
                    .build();
        }

        stage.close();

        if(loadedDataId > 0) {
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