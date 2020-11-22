package moe.ofs.addon.navdata.controllers;

import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.NavaidPageObject;
import moe.ofs.addon.navdata.domain.Waypoint;
import moe.ofs.addon.navdata.domain.WaypointPageObject;
import moe.ofs.addon.navdata.services.NavaidService;
import moe.ofs.addon.navdata.services.WaypointService;
import moe.ofs.backend.pagination.PageVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("navdata")
public class NavDataController {

    private final NavaidService navaidService;
    private final WaypointService waypointService;

    public NavDataController(NavaidService navaidService, WaypointService waypointService) {
        this.navaidService = navaidService;
        this.waypointService = waypointService;
    }

//    @PostMapping("/navaid/page")
//    PageVo<Navaid> getNavaids(@RequestBody PageObject pageObject) {
//        return navaidService.findPage(pageObject);
//    }

    @PostMapping("/navaid/keyword")
    PageVo<Navaid> getNavaidsByKeyword(@RequestBody NavaidPageObject pageObject) {
        return pageObject.getKeyword() != null ? navaidService.findPageByKeyword(pageObject) :
                navaidService.findPage(pageObject) ;
    }

    @PostMapping("/waypoint/keyword")
    PageVo<Waypoint> getWaypointsByKeyword(@RequestBody WaypointPageObject pageObject) {
        return pageObject.getKeyword() != null ? waypointService.findPageByKeyword(pageObject) :
                waypointService.findPage(pageObject) ;
    }
}
