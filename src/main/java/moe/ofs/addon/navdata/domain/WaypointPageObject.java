package moe.ofs.addon.navdata.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import moe.ofs.backend.pagination.PageObject;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class WaypointPageObject extends PageObject {
    private String code;

    private String keyword;
}
