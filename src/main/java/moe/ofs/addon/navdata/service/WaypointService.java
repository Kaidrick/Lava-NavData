package moe.ofs.addon.navdata.service;

import moe.ofs.addon.navdata.domain.Waypoint;

import java.util.List;

public interface WaypointService {
     List<Waypoint> findAllByCode(String code);
}
