package moe.ofs.addon.navdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.gui.controllers.MainAnchorPane;
import moe.ofs.addon.navdata.services.NavaidService;
import moe.ofs.addon.navdata.services.WaypointService;
import moe.ofs.backend.Plugin;
import moe.ofs.backend.UTF8Control;
import moe.ofs.backend.handlers.MissionStartObservable;
import moe.ofs.backend.request.server.ServerDataRequest;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NavData implements Plugin {

    private static final String name = "Navigation Data Provider";
    private static final String desc = "Provide waypoint and navaid data to other addons";

    private final NavaidService navaidService;
    private final WaypointService waypointService;

    private final MainAnchorPane mainAnchorPane;

    private Parent gui;

    // autowired from spring text; bean created by lava application main method
    private final FxWeaver fxWeaver;

    public NavData(NavaidService navaidService, WaypointService waypointService, MainAnchorPane mainAnchorPane, FxWeaver fxWeaver) {
        this.navaidService = navaidService;
        this.waypointService = waypointService;

        this.mainAnchorPane = mainAnchorPane;

        this.fxWeaver = fxWeaver;
    }

    private MissionStartObservable missionStartObservable;

    private void loadData(String regionName) throws IOException {
        List<Waypoint> waypoints = DataParser.parseWaypoints();
        List<Navaid> navaids = DataParser.parseNavaids();

        // called after every background task restart
        try(InputStream inputStream = getClass().getResourceAsStream("/region_border.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            Type mapForRegionBorderType = new TypeToken<Map<String, RegionBorder>>() {}.getType();
            Map<String, RegionBorder> borderMap = new Gson().fromJson(bufferedReader.lines()
                    .collect(Collectors.joining("\n")), mapForRegionBorderType);

            RegionBorder border = borderMap.get(regionName);
            System.out.println(borderMap.get(regionName));

            filterByRegion(navaids, border).forEach(navaidService::save);
            filterByRegion(waypoints, border).forEach(waypointService::save);
        }
    }

    private <T extends NavFix> List<T> filterByRegion(List<T> fixes, RegionBorder border) {
        return fixes.stream().filter(n -> {
            double lat = n.getPosition().getLatitude();
            double lon = n.getPosition().getLongitude();
            return lat > border.getSouth() && lat < border.getNorth() &&
                    lon > border.getWest() && lon < border.getEast();
        }).collect(Collectors.toList());
    }

    @Override
    public void register() {
        missionStartObservable = theaterName -> {
            // define region to be loaded
            // pretend theater name is not available
            String regionName = new ServerDataRequest("return env.mission.theatre").get();
            log.info(String.format("Load navigation data for %s region", regionName));

            try {
                loadData(regionName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        missionStartObservable.register();
    }

    @Override
    public void unregister() {
        missionStartObservable.unregister();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public Parent getPluginGui() throws IOException {
        if(gui == null) {
            System.out.println("fxWeaver = " + fxWeaver);
            ResourceBundle resourceBundle =
                    ResourceBundle.getBundle("MainAnchorPane", Locale.CHINA, new UTF8Control());
            gui = fxWeaver.loadView(MainAnchorPane.class, resourceBundle);
        }
        return gui;
    }
}
