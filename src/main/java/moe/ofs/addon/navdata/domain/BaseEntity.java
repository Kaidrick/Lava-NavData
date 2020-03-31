package moe.ofs.addon.navdata.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public abstract class BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
}
