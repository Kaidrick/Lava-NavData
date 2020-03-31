package moe.ofs.addon.navdata;

import lombok.extern.slf4j.Slf4j;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.backend.Plugin;
import moe.ofs.backend.PluginClassLoader;
import moe.ofs.backend.handlers.MissionStartObservable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class NavData implements Plugin {

    private final String name = "Navigation Data Provider";
    private final String desc = "Provide waypoint and navaid data to other addons";

    private boolean isLoaded;

    private void loadData() throws IOException {
        List<Waypoint> waypoints = DataParser.parseWaypoints();
        List<Navaid> navaids = DataParser.parseNavaids();
        // called after every background task restart
        // get theater name
        MissionStartObservable missionStartObservable = theaterName -> {

        };
    }

    @Override
    public void register() {
        isLoaded = true;
        writeConfiguration("enabled", "true");
    }

    @Override
    public void unregister() {
        isLoaded = false;
        writeConfiguration("enabled", "false");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @PostConstruct
    @Override
    public void init() {
        System.out.println("NavData plugin constructed");
        Plugin.super.init();
        PluginClassLoader.loadedPluginSet.add(this);
    }
}
