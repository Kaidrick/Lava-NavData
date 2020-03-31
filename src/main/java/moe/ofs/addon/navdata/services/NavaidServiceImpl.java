package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.backend.services.map.AbstractMapService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavaidServiceImpl extends AbstractMapService<Navaid> implements NavaidService {
    @Override
    public List<Navaid> findAllByCode(String code) {
        return null;
    }
}
