package moe.ofs.addon.navdata.services;

import lombok.extern.slf4j.Slf4j;
import moe.ofs.addon.navdata.domain.Navaid;
import moe.ofs.addon.navdata.domain.NavaidPageObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class NavaidServiceImplTest {

    NavaidService navaidService = new NavaidServiceImpl();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 2000000; i++) {
            Navaid navaid = new Navaid();
            navaid.setId(i + 1L);
            navaid.setName("Navaid " + (i + 1));
            navaid.setFrequency(124000);

            navaidService.save(navaid);
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findPage() {
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
        NavaidPageObject pageObject = new NavaidPageObject();
        pageObject.setCurrentPageNo(410L);
        pageObject.setName("Placeholder");
        pageObject.setPageSize(300);

        navaidService.findPage(pageObject);
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
    }
}