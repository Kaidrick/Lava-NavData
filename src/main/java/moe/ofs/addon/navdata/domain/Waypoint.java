package moe.ofs.addon.navdata.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Waypoint extends NavFix {

    // ADALE|42853011|-86010992|K5
    private String nationalityCode;

    @Override
    public String toString() {
        return getCode() + " (" + getPosition().getLatitude() + ", " + getPosition().getLongitude() + ")";
    }
}
