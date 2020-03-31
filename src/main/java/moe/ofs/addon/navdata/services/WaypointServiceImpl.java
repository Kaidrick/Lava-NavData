package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.backend.services.map.AbstractMapService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaypointServiceImpl extends AbstractMapService<Waypoint> implements WaypointService {
    @Override
    public List<Waypoint> findAllByCode(String code) {
        return null;
    }
}
