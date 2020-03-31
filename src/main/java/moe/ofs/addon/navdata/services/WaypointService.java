package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.backend.services.CrudService;

import java.util.List;

public interface WaypointService extends CrudService<Waypoint> {
     List<Waypoint> findAllByCode(String code);
}
