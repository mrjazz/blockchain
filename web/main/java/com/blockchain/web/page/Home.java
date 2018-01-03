package com.blockchain.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by denis on 1/3/2018.
 */
@Controller
public class Home {

    @RequestMapping("/")
    public String index() {
        return "pages/home";
    }

}
