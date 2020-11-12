package moe.ofs.addon.navdata;

import lombok.extern.slf4j.Slf4j;

import moe.ofs.backend.Plugin;

import moe.ofs.backend.handlers.MissionStartObservable;
import moe.ofs.backend.request.server.ServerDataRequest;


import moe.ofs.backend.request.services.RequestTransmissionService;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class NavDataProvider implements Plugin {

    public static final String name = "Navigation Data Provider";
    public static final String desc = "Provide waypoint and navaid data to other addons";

    private final RequestTransmissionService requestTransmissionService;

    private final DataManager manager;

    private MissionStartObservable missionStartObservable;

    // autowired from spring context; bean created by lava application main method
    public NavDataProvider(RequestTransmissionService requestTransmissionService, DataManager manager) {
        this.requestTransmissionService = requestTransmissionService;
        this.manager = manager;

    }

    @Override
    public void register() {
        log.info("Registering Navigational Data Provider Addon");

        // either listen to mission start topic in mq or hook to observable
        missionStartObservable = theaterName -> {
            // define region to be loaded
            // pretend theater name is not available
            requestTransmissionService.send(new ServerDataRequest("return env.mission.theatre")
                    .addProcessable(s -> {
                        log.info(String.format("Load navigation data for %s region", s));
                        try {
                            manager.unloadData();
                            manager.loadData(s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
        };
        missionStartObservable.register();
    }

    @Override
    public void unregister() {
        manager.unloadData();
//        missionStartObservable.unregister();
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
    public String getLocalizedName() {
//        ResourceBundle bundle =
//                ResourceBundle.getBundle("NavData", I18n.getLocale(), new UTF8Control());
//        return I18n.getString(bundle, name);

        return "NavData";
    }

    @Override
    public String getLocalizedDescription() {
//        ResourceBundle bundle =
//                ResourceBundle.getBundle("NavData", I18n.getLocale(), new UTF8Control());
//
//        return bundle.getString(desc);
        return "NavData description";
    }

//    @Override
//    public Parent getPluginGui() throws IOException {
//        if(gui == null) {
//            ResourceBundle resourceBundle =
//                    ResourceBundle.getBundle("NavData", new UTF8Control());
//            gui = fxWeaver.loadView(MainAnchorPane.class, resourceBundle);
//        }
//        return gui;
//    }

//    @Override
//    public Image getIcon() {
//        return new Image(getClass().getResourceAsStream("/nav-data-provider.png"));
//    }
}
