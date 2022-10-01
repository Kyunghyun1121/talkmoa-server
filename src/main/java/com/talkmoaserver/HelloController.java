package com.talkmoaserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public String hello() {
        return "hello! MJU Team Project 1 - Subject1 : Talkmoa REST API Server is Running!";
    }
}
