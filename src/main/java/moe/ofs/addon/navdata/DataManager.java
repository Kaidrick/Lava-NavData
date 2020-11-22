package moe.ofs.addon.navdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
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

@Slf4j
@Component
public class DataManager implements Configurable {

    private final NavaidService navaidService;
    private final WaypointService waypointService;
    private final ReferencePointService referencePointService;
    private final UserDataService<NavFix> userDataService;

    private final ReferencePointManager referencePointManager;

    // controllers

    public DataManager(NavaidService navaidService, WaypointService waypointService,
                       ReferencePointService referencePointService,
                       UserDataService<NavFix> userDataService,
                       ReferencePointManager referencePointManager
                       ) {

        this.navaidService = navaidService;
        this.waypointService = waypointService;
        this.referencePointService = referencePointService;
        this.userDataService = userDataService;
        this.referencePointManager = referencePointManager;


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

        }

        referencePointManager.getAll().forEach(referencePointService::save);

    }

    public void unloadData() {
        navaidService.deleteAll();
        waypointService.deleteAll();
        referencePointService.deleteAll();
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
