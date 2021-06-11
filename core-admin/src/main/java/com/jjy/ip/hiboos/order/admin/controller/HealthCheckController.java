package com.jjy.ip.hiboos.order.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping()
public class HealthCheckController {

    private static final String RESP_OK = "OK";

    @GetMapping("/health_check")
    public String healthCheck() {
        return RESP_OK;
    }
}
