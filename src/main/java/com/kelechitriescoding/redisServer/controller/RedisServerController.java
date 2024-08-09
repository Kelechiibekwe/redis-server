package com.kelechitriescoding.redisServer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/redis")
public class RedisServerController {

    @GetMapping
    public String getHelloWord(){
        return "Hello World";
    }
}