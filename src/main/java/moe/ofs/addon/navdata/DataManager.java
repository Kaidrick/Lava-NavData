package moe.ofs.addon.navdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.gui.controllers.NavAidsTitledPane;
import moe.ofs.addon.navdata.gui.controllers.ReferencePointDataTitledPane;
import moe.ofs.addon.navdata.gui.controllers.WaypointTitledPane;
import moe.ofs.addon.navdata.services.NavaidService;
import moe.ofs.addon.navdata.services.ReferencePointService;
import moe.ofs.addon.navdata.services.UserDataService;
import moe.ofs.addon.navdata.services.WaypointService;
import moe.ofs.backend.Configurable;
import moe.ofs.backend.function.refpoint.ReferencePointManager;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataManager implements Configurable {

    private final NavaidService navaidService;
    private final WaypointService waypointService;
    private final ReferencePointService referencePointService;
    private final UserDataService<NavFix> userDataService;

    private final ReferencePointManager referencePointManager;

    // controllers
    private final NavAidsTitledPane navAidsTitledPane;
    private final WaypointTitledPane waypointTitledPane;
    private final ReferencePointDataTitledPane referencePointDataTitledPane;

    public DataManager(NavaidService navaidService, WaypointService waypointService,
                       ReferencePointService referencePointService, UserDataService<NavFix> userDataService, ReferencePointManager referencePointManager, NavAidsTitledPane navAidsTitledPane, WaypointTitledPane waypointTitledPane, ReferencePointDataTitledPane referencePointDataTitledPane) {

        this.navaidService = navaidService;
        this.waypointService = waypointService;
        this.referencePointService = referencePointService;
        this.userDataService = userDataService;
        this.referencePointManager = referencePointManager;

        this.navAidsTitledPane = navAidsTitledPane;
        this.waypointTitledPane = waypointTitledPane;
        this.referencePointDataTitledPane = referencePointDataTitledPane;

        if(dataFileExists("user_nav_fix")) {
            List<NavFix> navFixList = readFile("user_nav_fix");
            navFixList.forEach(userDataService::save);
        }
    }

    public void loadData(String regionName) throws IOException {
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

            Platform.runLater(() -> {
                if(navAidsTitledPane.isInitialized()) {
                    navaidService.findAll().forEach(navAidsTitledPane::addToListView);
                }

                if(waypointTitledPane.isInitialized()) {
                    waypointService.findAll().forEach(waypointTitledPane::addToListView);
                }
            });
        }

        referencePointManager.getAll().forEach(referencePointService::save);
        Platform.runLater(() -> {
            if(referencePointDataTitledPane.isInitialized()) {
                referencePointService.findAll().forEach(referencePointDataTitledPane::addToBlueListView);
            }
        });
    }

    public void unloadData() {
        navaidService.deleteAll();
        waypointService.deleteAll();
        referencePointService.deleteAll();

        Platform.runLater(() -> {
            if(navAidsTitledPane.isInitialized()) {
                navAidsTitledPane.clearListView();
            }

            if(waypointTitledPane.isInitialized()) {
                waypointTitledPane.clearListView();
            }

            if(referencePointDataTitledPane.isInitialized()) {
                referencePointDataTitledPane.clearListViews();
            }
        });
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
    public String getName() {
        return NavDataProvider.name;
    }
}
