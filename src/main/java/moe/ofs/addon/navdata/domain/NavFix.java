package moe.ofs.addon.navdata.domain;

import lombok.Getter;
import lombok.Setter;
import moe.ofs.backend.domain.BaseEntity;
import moe.ofs.backend.object.map.GeoPosition;

@Getter
@Setter
public abstract class NavFix extends BaseEntity {
    private String code;
    private GeoPosition position;
}
