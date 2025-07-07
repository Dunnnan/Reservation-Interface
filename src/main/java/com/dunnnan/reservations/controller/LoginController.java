package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.model.dto.AppUserDto;
import com.dunnnan.reservations.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new AppUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("user") @Validated AppUserDto appUser,
            BindingResult result,
            Model model) {
        if (!appUser.getPassword().equals(appUser.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords don't match");
        }

        if (userService.emailExists(appUser.getEmail())) {
            result.rejectValue("email", "error.user", "Email is already registered");
        }

        if (result.hasErrors()) {
            return "register";
        }

        userService.registerUser(appUser);
        return "redirect:/login";
    }

}
