package moe.ofs.addon.navdata;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.backend.object.map.GeoPosition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataParser {

    public static List<String[]> getData(String fileName) throws IOException {
        try(InputStream inputStream = DataParser.class.getResourceAsStream(fileName)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().map(line -> line.split("\\|")).collect(Collectors.toList());
        }
    }

    public static List<Waypoint> parseWaypoints() throws IOException {

        List<Waypoint> list = new ArrayList<>();

        List<String[]> data = getData("/waypoints.txt");

        data.forEach(datum -> {
            Waypoint waypoint = new Waypoint();
            waypoint.setCode(datum[0]);

            GeoPosition position = new GeoPosition();
            position.setLatitude(Long.parseLong(datum[1]));
            position.setLongitude(Long.parseLong(datum[2]));
            waypoint.setNationalityCode(datum[3]);

            waypoint.setPosition(position);

            list.add(waypoint);
        });

        return list;
    }

    public static List<Navaid> parseNavaids() throws IOException {
        List<Navaid> list = new ArrayList<>();

        List<String[]> data = getData("/navaids.txt");

        data.forEach(datum -> {
            Navaid navaid = new Navaid();
            navaid.setCode(datum[0]);
            navaid.setName(datum[1]);
            navaid.setFrequency(Long.parseLong(datum[2]));
            // d1 d2 d3
            navaid.setRadial(Integer.parseInt(datum[3]));
            navaid.setD2(Integer.parseInt(datum[4]));

            GeoPosition position = new GeoPosition();
            position.setLatitude(Long.parseLong(datum[6]));
            position.setLongitude(Long.parseLong(datum[7]));
            position.setAltitude(Long.parseLong(datum[8]));
            navaid.setPosition(position);

            navaid.setNationalityCode(datum[9]);

            list.add(navaid);
        });

        return list;
    }
}
