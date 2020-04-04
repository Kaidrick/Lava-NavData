package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.backend.services.map.AbstractMapService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDataServiceImpl<T extends NavFix> extends AbstractMapService<T> implements UserDataService<T> {
    @Override
    public List<T> findAllByCode(String code) {
        return map.values().stream().filter(navfix ->
                navfix.getCode().equals(code)).collect(Collectors.toList());
    }


}