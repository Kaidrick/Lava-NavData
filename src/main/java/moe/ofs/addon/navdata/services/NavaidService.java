package moe.ofs.addon.navdata.services;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.backend.services.CrudService;

import java.util.List;

public interface NavaidService extends CrudService<Navaid> {
    List<Navaid> findAllByCode(String code);
}
