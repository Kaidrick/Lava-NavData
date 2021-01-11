package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.NavFix;
import moe.ofs.backend.common.CrudService;

import java.util.List;

public interface UserDataService<T extends NavFix> extends CrudService<T> {
    List<T> findAllByCode(String code);
}
