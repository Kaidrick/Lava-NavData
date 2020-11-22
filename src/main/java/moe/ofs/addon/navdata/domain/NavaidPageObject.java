package moe.ofs.addon.navdata.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import moe.ofs.backend.pagination.PageObject;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class NavaidPageObject extends PageObject {
    private String code;
    private String name;
    private Long frequency;
    private String keyword;
}
