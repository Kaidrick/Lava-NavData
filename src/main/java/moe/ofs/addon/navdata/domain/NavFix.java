package moe.ofs.addon.navdata.domain;

import lombok.*;
import moe.ofs.backend.domain.BaseEntity;
import moe.ofs.backend.object.map.GeoPosition;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class NavFix extends BaseEntity {
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
