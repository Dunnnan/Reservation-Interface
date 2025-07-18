package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.service.ReservationService;
import com.dunnnan.reservations.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/reserve")
    public String createReservation(
            @ModelAttribute("reservation") @Validated ReservationDto reservationDto,
            BindingResult result,
            Model model
    ) {

        result = reservationService.reserve(reservationDto, result);

        if (result.hasErrors()) {
            model.addAttribute("resource", resourceService.getResourceById(reservationDto.getResourceId()).orElse(null));
            return "resource-detail";
        }

        return "redirect:/resource/" + reservationDto.getResourceId();
    }

}
