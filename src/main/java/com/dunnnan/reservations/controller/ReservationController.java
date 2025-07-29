package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.service.ReservationService;
import com.dunnnan.reservations.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        result = reservationService.reserve(reservationDto, result);

        if (result.hasErrors()) {
            Optional<Resource> resource = resourceService.getResourceById(reservationDto.getResourceId());

            if (resource.isEmpty()) {
                return "redirect:/home";
            }

            model.addAttribute("resource", resource.get());
            return "resource-detail";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Reservation successful!");
        return "redirect:/resource/" + reservationDto.getResourceId();
    }

    @GetMapping("/reservations")
    public String getReservations(
            @RequestParam(name = "reservationId", required = false) String idOfReservationToBeDeleted,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return "my-reservations";
    }

    @GetMapping("/reservations/availableHours")
    @ResponseBody
    public List<String> getAvailableHours(
            @RequestParam Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return reservationService.getValidReservationHours(resourceId, date).stream()
                .map(LocalTime::toString)
                .toList();
    }

}
