package moe.ofs.addon.navdata;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {
    /**
     * Use a controller to serve a custom page for this plugin / addon
     * @return String view name
     */
    @RequestMapping(method = RequestMethod.GET, value = "/navdata")
    public String test() {
        return "test.html";
    }
}
