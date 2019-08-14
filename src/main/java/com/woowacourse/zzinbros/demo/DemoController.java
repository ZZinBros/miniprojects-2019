package com.woowacourse.zzinbros.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/")
    public String demo(){
        return "index";
    }
}
