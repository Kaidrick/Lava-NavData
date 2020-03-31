package moe.ofs.addon.navdata.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Waypoint extends NavFix {

    // ADALE|42853011|-86010992|K5
    private String nationalityCode;
}
