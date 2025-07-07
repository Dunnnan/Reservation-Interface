package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("resources", resourceService.getAllResources());
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
