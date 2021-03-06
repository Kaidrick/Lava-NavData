package moe.ofs.addon.navdata.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import moe.ofs.addon.navdata.NavDataProvider;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.services.UserDataService;
import moe.ofs.backend.UTF8Control;
import moe.ofs.backend.object.map.GeoPosition;
import moe.ofs.backend.object.map.GeoPositions;
import moe.ofs.backend.object.map.Orientation;
import moe.ofs.backend.object.unitofmeasure.Length;
import moe.ofs.backend.util.I18n;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

@Controller
@FxmlView
public class UserDataCreationDialog implements Initializable {

    private Stage stage;

    private final NavDataProvider navDataProvider;

    @FXML private AnchorPane userDataCreationPane;
    @FXML private VBox mainVBox;
    @FXML private HBox fixNameHBox;

    @FXML private VBox positionVBox;
    @FXML private HBox altitudeHBox;

    @FXML private TextField identificationTextField;
    @FXML private TextField fixNameTextField;

    @FXML private HBox latitudeHBox;
    @FXML private HBox longitudeHBox;

    @FXML private TextField latitudeDegreeTextField;
    @FXML private TextField latitudeMinuteTextField;
    @FXML private TextField latitudeSecondTextField;

    @FXML private TextField longitudeDegreeTextField;
    @FXML private TextField longitudeMinuteTextField;
    @FXML private TextField longitudeSecondTextField;

    @FXML private TextField altitudeTextField;

    @FXML private Label latitudeSecondLabel;
    @FXML private Label longitudeSecondLabel;

    @FXML private TextArea fixPointDescriptionTextArea;

    @FXML private RadioButton dataTypeNavaidRadioButton;
    @FXML private RadioButton dataTypeWaypointRadioButton;
    @FXML private RadioButton dataFormatStandardRadioButton;
    @FXML private RadioButton dataFormatPrecisionRadioButton;

    @FXML private ComboBox<Orientation> latitudeOrientComboBox;
    @FXML private ComboBox<Orientation> longitudeOrientComboBox;

    @FXML private ComboBox<Length> altitudeUnitComboBox;

    @FXML private Button navFixUserDataSubmitButton;

    private ToggleGroup dataTypeToggleGroup = new ToggleGroup();
    private ToggleGroup dataFormatToggleGroup = new ToggleGroup();

    private NavFix data;

    private final UserDataService<NavFix> userDataService;
    private final UserDataTitledPane userDataTitledPane;

    public UserDataCreationDialog(NavDataProvider navDataProvider, UserDataService<NavFix> userDataService, UserDataTitledPane userDataTitledPane) {
        this.navDataProvider = navDataProvider;
        this.userDataService = userDataService;
        this.userDataTitledPane = userDataTitledPane;
    }

    private boolean listenerAdded;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // text fields to be shown depends on data precision type
        // if using precision format, hide seconds textfield
        // and replace text formatter

        System.out.println("init user data form dialog");
        stage = new Stage();
        Scene scene = new Scene(userDataCreationPane);
        stage.getIcons().add(navDataProvider.getIcon());

        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);

        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);

        // restrict text field input
        // TODO -> terrible regex pattern
        UnaryOperator<TextFormatter.Change> latDegree = change ->
                change.getControlNewText().matches("^[0][0-9]?$|^[1-8][0-9]$|^[1-9]$|^$") ? change : null;

        UnaryOperator<TextFormatter.Change> standardMinuteSecond = change ->
                change.getControlNewText().matches("^[0][0-9]?$|^[1-5][0-9]$|^[1-9]$|^$") ?
                        change : null;

        UnaryOperator<TextFormatter.Change> lonDegree = change ->
                change.getControlNewText().matches("^[0][0-9]?$|^[1-8][0-9]$|^[1][0-7][0-9]$|^[1-9]$|^$") ?
                        change : null;

        UnaryOperator<TextFormatter.Change> altRestrict = change ->
                change.getControlNewText().matches(
                        "^((?<!\\w)-?(?:[\\d]?|[1-9][\\d]*)(?<!\\s)(?:[.]\\d+))$" + "|" +
                                "^([\\d]|[1-9][\\d])$" + "|" +
                                "^-?(?:[0]|[1-9]*)\\.?$" + "|" +
                                "^$") ?
                        change : null;

        UnaryOperator<TextFormatter.Change> precisionMinute = change ->
                change.getControlNewText().matches(
                        "^((?<!\\w)(?:[\\d]?|[1-9][\\d]*)(?<!\\s)(?:[.]\\d+))$" + "|" +
                                "^([\\d]|[1-9][\\d])$" + "|" +
                                "^(?:[0]|[1-9]*)\\.?$" + "|" +
                                "^$") ?
                        change : null;

        dataTypeToggleGroup.getToggles().addAll(dataTypeNavaidRadioButton, dataTypeWaypointRadioButton);
        dataFormatToggleGroup.getToggles().addAll(dataFormatStandardRadioButton, dataFormatPrecisionRadioButton);

        latitudeOrientComboBox.getItems().addAll(Orientation.NORTH, Orientation.SOUTH);
        longitudeOrientComboBox.getItems().addAll(Orientation.EAST, Orientation.WEST);

        altitudeUnitComboBox.getItems().addAll(Length.METERS, Length.FEET);

        latitudeDegreeTextField.setTextFormatter(new TextFormatter<>(latDegree));
        latitudeSecondTextField.setTextFormatter(new TextFormatter<>(standardMinuteSecond));

        longitudeDegreeTextField.setTextFormatter(new TextFormatter<>(lonDegree));
        longitudeSecondTextField.setTextFormatter(new TextFormatter<>(standardMinuteSecond));

        altitudeTextField.setTextFormatter(new TextFormatter<>(altRestrict));

        if(!listenerAdded) {
            listenerAdded = true;

            // data format
            dataFormatToggleGroup.selectedToggleProperty().addListener(((observable) -> {

                // standard dd mm ss format
                if(dataFormatToggleGroup.getSelectedToggle().equals(dataFormatStandardRadioButton)) {
                    // if not in, add
                    // if already in, do nothing
                    if(!latitudeHBox.getChildren().contains(latitudeSecondTextField) &&
                            !latitudeHBox.getChildren().contains(latitudeSecondLabel)) {

                        latitudeMinuteTextField.setPrefWidth(latitudeMinuteTextField.getPrefWidth() -
                                latitudeSecondTextField.getPrefWidth());
                        latitudeHBox.getChildren().addAll(latitudeSecondTextField, latitudeSecondLabel);
                    }

                    if(!longitudeHBox.getChildren().contains(longitudeSecondTextField) &&
                            !longitudeHBox.getChildren().contains(longitudeSecondLabel)) {

                        longitudeMinuteTextField.setPrefWidth(longitudeMinuteTextField.getPrefWidth() -
                                longitudeSecondTextField.getPrefWidth());
                        longitudeHBox.getChildren().addAll(longitudeSecondTextField, longitudeSecondLabel);
                    }



                    latitudeMinuteTextField.setTextFormatter(new TextFormatter<>(standardMinuteSecond));
                    longitudeMinuteTextField.setTextFormatter(new TextFormatter<>(standardMinuteSecond));
                }
                // precision dd mm.mmm format
                else {
                    latitudeHBox.getChildren().removeAll(latitudeSecondTextField, latitudeSecondLabel);
                    longitudeHBox.getChildren().removeAll(longitudeSecondTextField, longitudeSecondLabel);

                    latitudeMinuteTextField.setPrefWidth(latitudeMinuteTextField.getPrefWidth() +
                            latitudeSecondTextField.getPrefWidth());
                    longitudeMinuteTextField.setPrefWidth(longitudeMinuteTextField.getPrefWidth() +
                            longitudeSecondTextField.getPrefWidth());

                    latitudeMinuteTextField.setTextFormatter(new TextFormatter<>(precisionMinute));
                    longitudeMinuteTextField.setTextFormatter(new TextFormatter<>(precisionMinute));
                }
            }));

            final int fixNameIndex = mainVBox.getChildren().indexOf(fixNameHBox);
            // data type
            dataTypeToggleGroup.selectedToggleProperty().addListener((observable -> {
                if(dataTypeToggleGroup.getSelectedToggle().equals(dataTypeNavaidRadioButton)) {
                    if(!mainVBox.getChildren().contains(fixNameHBox)) {
                        mainVBox.getChildren().add(fixNameIndex, fixNameHBox);
                    }

                    if(!positionVBox.getChildren().contains(altitudeHBox)) {
                        positionVBox.getChildren().add(altitudeHBox);
                    }
                } else {
                    mainVBox.getChildren().remove(fixNameHBox);
                    positionVBox.getChildren().remove(altitudeHBox);
                }
            }));

        }

        dataTypeToggleGroup.selectToggle(dataTypeNavaidRadioButton);
        dataFormatToggleGroup.selectToggle(dataFormatStandardRadioButton);


        ResourceBundle resourceBundle = ResourceBundle.getBundle("NavData",
                I18n.getLocale(), new UTF8Control());
        I18n.toPaneOrNotToPane(userDataCreationPane, resourceBundle);

//        // register to i18n change event
//        I18n.localeProperty().addListener((observable -> {
//            ResourceBundle resourceBundle = ResourceBundle.getBundle("NavData");
//            I18n.toPaneOrNotToPane(userDataCreationPane, resourceBundle);
//        }));
    }


    public void show() {
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public Stage loadData(NavFix fix) {
        data = fix;
        identificationTextField.setText(data.getCode());
        fixPointDescriptionTextArea.setText(data.getDescription());

        altitudeUnitComboBox.getSelectionModel().select(Length.FEET);

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
            System.out.println(data.getPosition().getAltitude());
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

        if(!validateInputData()) {
            // get a dialog showing error message? or use something for error message
            ResourceBundle bundle = ResourceBundle.getBundle("NavData", I18n.getLocale(), new UTF8Control());
            Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getString(bundle,
                    "nav_fix_entry_incomplete_data"), ButtonType.OK);
            alert.initOwner(stage);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();

            return;
        }

        // insert orientation
        GeoPosition geoPosition;
        if(dataFormatToggleGroup.getSelectedToggle().equals(dataFormatStandardRadioButton)) {
            geoPosition = GeoPositions.get(
                    latitudeOrientComboBox.getValue(),
                    latitudeDegreeTextField.getText(),
                    latitudeMinuteTextField.getText(),
                    latitudeSecondTextField.getText(),

                    longitudeOrientComboBox.getValue(),
                    longitudeDegreeTextField.getText(),
                    longitudeMinuteTextField.getText(),
                    longitudeSecondTextField.getText()
            );
        } else {
            geoPosition = GeoPositions.get(
                    latitudeOrientComboBox.getValue(),
                    latitudeDegreeTextField.getText(),
                    latitudeMinuteTextField.getText(),
                    "0",

                    longitudeOrientComboBox.getValue(),
                    longitudeDegreeTextField.getText(),
                    longitudeMinuteTextField.getText(),
                    "0"
            );
        }


        if(dataTypeToggleGroup.getSelectedToggle().equals(dataTypeNavaidRadioButton)) {
            geoPosition.setAltitude(Double.parseDouble(altitudeTextField.getText()));

            // need a converter here to convert LL(different format)/LO
            data = Navaid.builder()
                    .code(identificationTextField.getText())
                    .name(fixNameTextField.getText())
                    .position(geoPosition)
                    .description(fixPointDescriptionTextArea.getText())
                    .build();

        } else {
            data = Waypoint.builder()
                    .code(identificationTextField.getText())
                    .position(geoPosition)
                    .description(fixPointDescriptionTextArea.getText())
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

    private boolean validateInputData() {
        // check mode
        // if standard
        //      if waypoint -> alt and fix name can be empty
        //      if navaid -> everything must be filled up
        // if precision
        //      if waypoint -> alt, fix name and lat/lon seconds can be empty
        //      if navaid -> lat/lon seconds can be empty

        boolean commonCheck = !identificationTextField.getText().equals("") &&
                latitudeOrientComboBox.getSelectionModel().getSelectedItem() != null &&
                longitudeOrientComboBox.getSelectionModel().getSelectedItem() != null &&

                !latitudeDegreeTextField.getText().equals("") &&
                !latitudeMinuteTextField.getText().equals("") &&

                !longitudeDegreeTextField.getText().equals("") &&
                !longitudeMinuteTextField.getText().equals("");

        if(dataFormatToggleGroup.getSelectedToggle().equals(dataFormatStandardRadioButton)) {
            if(dataTypeToggleGroup.getSelectedToggle().equals(dataTypeWaypointRadioButton)) {
                return commonCheck &&
                        !latitudeSecondTextField.getText().equals("") &&
                        !longitudeSecondTextField.getText().equals("");

            } else {
                return commonCheck &&
                        !fixNameTextField.getText().equals("") &&
                        !altitudeTextField.getText().equals("") &&
                        altitudeUnitComboBox.getSelectionModel().getSelectedItem() != null &&

                        !latitudeSecondTextField.getText().equals("") &&
                        !longitudeSecondTextField.getText().equals("");
            }
        } else {
            if(dataTypeToggleGroup.getSelectedToggle().equals(dataTypeWaypointRadioButton)) {
                return commonCheck;
            } else {
                return commonCheck &&
                        !fixNameTextField.getText().equals("") &&
                        !altitudeTextField.getText().equals("") &&
                        altitudeUnitComboBox.getSelectionModel().getSelectedItem() != null;
            }
        }
    }

}
