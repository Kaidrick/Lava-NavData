package moe.ofs.addon.navdata.service;

import moe.ofs.addon.navdata.domain.Navaid;

import java.util.List;

public interface NavaidService {
    List<Navaid> findAllByCode(String code);
}
