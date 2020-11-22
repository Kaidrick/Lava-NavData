package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.domain.WaypointPageObject;
import moe.ofs.backend.pagination.PageVo;
import moe.ofs.backend.services.map.AbstractPageableMapService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class WaypointServiceImpl extends AbstractPageableMapService<Waypoint> implements WaypointService {
    @Override
    public List<Waypoint> findAllByCode(String code) {
        return null;
    }

    @Override
    public PageVo<Waypoint> findPageByKeyword(WaypointPageObject pageObject) {
        Predicate<Waypoint> predicate = navaid ->
                pageObject.getKeyword() != null &&
                        (navaid.getCode().toLowerCase().contains(pageObject.getKeyword().toLowerCase()));

        return findPageByCriteria(pageObject, predicate);
    }
}
