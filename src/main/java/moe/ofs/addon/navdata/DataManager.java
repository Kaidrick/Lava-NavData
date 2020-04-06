package moe.ofs.addon.navdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.services.NavaidService;
import moe.ofs.addon.navdata.services.WaypointService;
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
public class DataManager {

    private final NavaidService navaidService;
    private final WaypointService waypointService;

    public DataManager(NavaidService navaidService, WaypointService waypointService) {
        this.navaidService = navaidService;
        this.waypointService = waypointService;
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
    }

    public void unloadData() {
        navaidService.deleteAll();
        waypointService.deleteAll();
    }

    private <T extends NavFix> List<T> filterByRegion(List<T> fixes, RegionBorder border) {
        return fixes.stream().filter(n -> {
            double lat = n.getPosition().getLatitude();
            double lon = n.getPosition().getLongitude();
            return lat > border.getSouth() && lat < border.getNorth() &&
                    lon > border.getWest() && lon < border.getEast();
        }).collect(Collectors.toList());
    }


    private void loadRefPoints() {
        // parse data and gson to

    }
}
