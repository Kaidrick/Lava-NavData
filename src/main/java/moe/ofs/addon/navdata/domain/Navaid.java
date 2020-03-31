package moe.ofs.addon.navdata.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Navaid extends NavFix {
    // HB|BELFAST CITY|420000|0|0|195|54621136|-5871908|15|EG

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
}
