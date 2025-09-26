package com.sdsu.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    
    @GetMapping("/hello")
    public String hello() {
        return "<h1 style='font-size: 50px; color: green;'>Backend is working boys!!!</h1>";
    }
}
