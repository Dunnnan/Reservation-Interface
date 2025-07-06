package com.dunnnan.reservations.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class ResourcesController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

//    @PostMapping("/home")
//    public String addResource() {
//
//    }
//
//
//    @PostMapping("/home/add")


}
