package com.coupon.coupon_project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerTestController {

    private static final String status = "deactivate";

    @GetMapping("/")
    public String test() {
        return "Hello, Docker is "+status+"!";
    }
}
