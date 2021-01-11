package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.backend.domain.dcs.theater.ReferencePoint;

import java.util.Set;

public interface NavFixSearchService {
    Set<NavFix> findAnyByCode(String ident);

    Set<NavFix> findWaypointByCode(String ident);

    Set<NavFix> findNavaidByCode(String ident);

    Set<ReferencePoint> findRefPointByCode(String name);
}
