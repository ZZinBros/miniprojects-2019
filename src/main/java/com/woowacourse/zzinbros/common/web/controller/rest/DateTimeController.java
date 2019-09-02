package com.woowacourse.zzinbros.common.web.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class DateTimeController {
    @GetMapping("/datetime")
    public String datetimeService() {
        return "{ \"datetime\": \""
                + Instant.now().toString()
                + "\" }";
    }
}
