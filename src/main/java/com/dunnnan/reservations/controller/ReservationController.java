package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.model.dto.ReservationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReservationController {

    @PostMapping("/reservation")
    public String createReservation(
            @ModelAttribute("reservation") @Validated ReservationDto reservationDto,
            BindingResult result,
            Model model
    ) {

        return "/resource/{id}";
    }

}
