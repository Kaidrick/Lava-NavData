package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.NavaidPageObject;
import moe.ofs.backend.common.AbstractPageableMapService;
import moe.ofs.backend.domain.pagination.PageVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class NavaidServiceImpl extends AbstractPageableMapService<Navaid> implements NavaidService {

    @Override
    public List<Navaid> findAllByCode(String code) {
        return null;
    }

    @Override
    public PageVo<Navaid> findPageByKeyword(NavaidPageObject pageObject) {
        Predicate<Navaid> predicate = navaid ->
                pageObject.getKeyword() != null &&
                        (navaid.getName().toLowerCase().contains(pageObject.getKeyword().toLowerCase()) ||
                        navaid.getCode().toLowerCase().contains(pageObject.getKeyword().toLowerCase()) ||
                        String.valueOf(navaid.getFrequency()).toLowerCase()
                                .contains(pageObject.getKeyword().toLowerCase()
                                        .replace(".", "")));

        return findPageByCriteria(pageObject, predicate);
    }
}
