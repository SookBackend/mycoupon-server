package com.coupon.coupon_project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerTestController {

    @GetMapping("/")
    public String test() {
        return "Hello, Docker!";
    }
}
