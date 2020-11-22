package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.NavaidPageObject;
import moe.ofs.backend.pagination.PageVo;
import moe.ofs.backend.services.ByPageSearchable;
import moe.ofs.backend.services.CrudService;

import java.util.List;

public interface NavaidService extends CrudService<Navaid>, ByPageSearchable<Navaid> {
    List<Navaid> findAllByCode(String code);

    PageVo<Navaid> findPageByKeyword(NavaidPageObject pageObject);
}
