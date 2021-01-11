package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.NavaidPageObject;
import moe.ofs.backend.common.ByPageSearchable;
import moe.ofs.backend.common.CrudService;
import moe.ofs.backend.domain.pagination.PageVo;

import java.util.List;

public interface NavaidService extends CrudService<Navaid>, ByPageSearchable<Navaid> {
    List<Navaid> findAllByCode(String code);

    PageVo<Navaid> findPageByKeyword(NavaidPageObject pageObject);
}
