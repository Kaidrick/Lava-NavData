package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.domain.WaypointPageObject;
import moe.ofs.backend.pagination.PageVo;
import moe.ofs.backend.services.ByPageSearchable;
import moe.ofs.backend.services.CrudService;

import java.util.List;

public interface WaypointService extends CrudService<Waypoint>, ByPageSearchable<Waypoint> {
     List<Waypoint> findAllByCode(String code);

     PageVo<Waypoint> findPageByKeyword(WaypointPageObject pageObject);
}
