package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.backend.object.map.GeoPosition;
import moe.ofs.backend.object.map.ReferencePoint;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NavFixSearchServiceImpl implements NavFixSearchService {
    private final NavaidService navaidService;
    private final WaypointService waypointService;
    private final UserDataService<NavFix> userDataService;
    private final ReferencePointService referencePointService;

    public NavFixSearchServiceImpl(NavaidService navaidService,
                                   WaypointService waypointService,
                                   UserDataService<NavFix> userDataService,
                                   ReferencePointService referencePointService) {
        this.navaidService = navaidService;
        this.waypointService = waypointService;
        this.userDataService = userDataService;
        this.referencePointService = referencePointService;
    }

    @Override
    public Set<NavFix> findAnyByCode(String ident) {
        List<NavFix> navFixList = Stream.of(navaidService.findAll(), waypointService.findAll(), userDataService.findAll())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Waypoint> refPointList = referencePointService.findAll().stream()
                .map(refPoint -> Waypoint.builder()
                        .code(refPoint.getName())
                        .position(new GeoPosition(refPoint.getLatitude(), refPoint.getLongitude()))
                        .build())
                .collect(Collectors.toList());

        return Stream.of(navFixList, refPointList)
                .flatMap(Collection::stream)
                .filter(n -> n.getCode().toLowerCase().contains(ident.toLowerCase()))
                .collect(Collectors.toSet());

//        return navFixList.stream().filter(n -> n.getCode().contains(ident)).collect(Collectors.toSet());
    }

    @Override
    public Set<NavFix> findWaypointByCode(String ident) {
        return null;
    }

    @Override
    public Set<NavFix> findNavaidByCode(String ident) {
        return null;
    }

    @Override
    public Set<ReferencePoint> findRefPointByCode(String name) {
        return null;
    }
}
