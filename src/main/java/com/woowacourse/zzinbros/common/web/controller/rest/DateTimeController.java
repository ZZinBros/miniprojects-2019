package com.woowacourse.zzinbros.common.web.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class DateTimeController {
    @GetMapping(path = "/datetime", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String datetimeService() {
        return "{ \"datetime\": \""
                + Instant.now().toString()
                + "\" }";
    }
}
