package moe.ofs.addon.navdata.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import moe.ofs.backend.object.map.GeoPosition;

@Getter
@Setter
@ToString
public class Waypoint extends BaseEntity{

    // ADALE|42853011|-86010992|K5
    private String code;
    private GeoPosition position;
    private String nationalityCode;

}
