package moe.ofs.addon.navdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegionBorder {
    private long north;
    private long south;
    private long east;
    private long west;
}
