package moe.ofs.addon.navdata.domain;

import lombok.*;
import moe.ofs.backend.object.map.GeoPosition;
import moe.ofs.backend.object.map.GeoPositions;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Navaid extends NavFix implements Serializable {
    // HB|BELFAST CITY|420000|0|0|195|54621136|-5871908|15|EG
    static final long serialVersionUID = 4321412L;


    /*
    * 0|0 -> NDB?
    * 0|1 -> 	NDB
    * 1|1 -> VOR/DME
    * 1|0 -> VOR/DME
    */
    private String name;
    private long frequency;
    private int radial;
    private int d2;
//    private int d3;  // useless data -> constant 195

    // should be area code
    private String nationalityCode;

    @Builder
    public Navaid(String code, GeoPosition position, String description, String name, long frequency) {
        super(code, position, description);
        this.name = name;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return getCode() + " (" + getName() + ") - " + GeoPositions.toLatLonAltDisplay(getPosition());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Navaid navaid = (Navaid) o;

        return Objects.equals(name, navaid.name) && super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
