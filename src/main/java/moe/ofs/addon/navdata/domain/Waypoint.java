package moe.ofs.addon.navdata.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moe.ofs.backend.object.map.GeoPosition;
import moe.ofs.backend.object.map.GeoPositions;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Waypoint extends NavFix implements Serializable {

    static final long serialVersionUID = 4321412L;

    // ADALE|42853011|-86010992|K5
    private String nationalityCode;

    @Builder
    public Waypoint(String code, GeoPosition position, String description, String nationalityCode) {
        super(code, position, description);
        this.nationalityCode = nationalityCode;
    }

    @Override
    public String toString() {
        return getCode() + " - " + GeoPositions.toLatLonDisplay(getPosition());
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
