package com.ntt.JobPool.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController()
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello Truc Nguyen";
    }

}
