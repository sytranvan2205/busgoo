package com.iuh.busgoo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/test")
    public String test() {
        return "test security";
    }
}
