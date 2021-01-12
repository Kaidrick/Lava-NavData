package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.domain.WaypointPageObject;
import moe.ofs.backend.common.ByPageSearchable;
import moe.ofs.backend.common.CrudService;
import moe.ofs.backend.domain.pagination.PageVo;

import java.util.List;

public interface WaypointService extends CrudService<Waypoint>, ByPageSearchable<Waypoint> {
     List<Waypoint> findAllByCode(String code);

     PageVo<Waypoint> findPageByKeyword(WaypointPageObject pageObject);
}
