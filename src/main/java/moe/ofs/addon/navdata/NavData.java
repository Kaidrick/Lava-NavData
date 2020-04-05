package moe.ofs.addon.navdata;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import moe.ofs.addon.navdata.gui.controllers.MainAnchorPane;
import moe.ofs.backend.Plugin;
import moe.ofs.backend.UTF8Control;
import moe.ofs.backend.handlers.MissionStartObservable;
import moe.ofs.backend.request.server.ServerDataRequest;
import moe.ofs.backend.util.I18n;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;

@Slf4j
@Component
public class NavData implements Plugin {

    private static final String name = "Navigation Data Provider";
    private static final String desc = "Provide waypoint and navaid data to other addons";

    private final DataManager manager;
    private final FxWeaver fxWeaver;

    private Parent gui;

    private MissionStartObservable missionStartObservable;

    // autowired from spring context; bean created by lava application main method
    public NavData(DataManager manager, FxWeaver fxWeaver) {
        this.manager = manager;

        this.fxWeaver = fxWeaver;
    }

    @Override
    public void register() {
        missionStartObservable = theaterName -> {
            // define region to be loaded
            // pretend theater name is not available
            String regionName = new ServerDataRequest("return env.mission.theatre").get();
            log.info(String.format("Load navigation data for %s region", regionName));

            try {
                manager.unloadData();
                manager.loadData(regionName);
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

    // try to return a localized string
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public String getLocalizedName() {
        ResourceBundle bundle =
                ResourceBundle.getBundle("NavData", I18n.getLocale(), new UTF8Control());
        return I18n.getString(bundle, name);
    }

    @Override
    public String getLocalizedDescription() {
        ResourceBundle bundle =
                ResourceBundle.getBundle("NavData", I18n.getLocale(), new UTF8Control());
        return I18n.getString(bundle, desc);
    }

    @Override
    public Parent getPluginGui() throws IOException {
        if(gui == null) {
            System.out.println("fxWeaver = " + fxWeaver);
            ResourceBundle resourceBundle =
                    ResourceBundle.getBundle("NavData", new UTF8Control());
            gui = fxWeaver.loadView(MainAnchorPane.class, resourceBundle);
        }
        return gui;
    }

    @Override
    public Image getIcon() {
        return new Image(getClass().getResourceAsStream("/nav-data-provider.png"));
    }
}
