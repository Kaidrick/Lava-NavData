package moe.ofs.addon.navdata.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moe.ofs.backend.object.map.GeoPosition;

@Getter
@Setter
@NoArgsConstructor
public class Waypoint extends NavFix {

    // ADALE|42853011|-86010992|K5
    private String nationalityCode;

    @Builder
    public Waypoint(String code, GeoPosition position, String nationalityCode) {
        super(code, position);
        this.nationalityCode = nationalityCode;
    }

    @Override
    public String toString() {
        return getCode() + " (" + getPosition().getLatitude() + ", " + getPosition().getLongitude() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
