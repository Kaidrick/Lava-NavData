package moe.ofs.addon.navdata.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moe.ofs.backend.domain.dcs.BaseEntity;
import moe.ofs.backend.domain.dcs.theater.GeoPosition;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class NavFix extends BaseEntity implements Serializable {
    private String code;
    private GeoPosition position;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavFix fix = (NavFix) o;

        if (!code.equals(fix.code)) return false;
        return position.equals(fix.position);
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }
}
