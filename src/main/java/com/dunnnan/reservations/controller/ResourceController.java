package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.config.PaginationConfig;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.model.dto.ResourceDto;
import com.dunnnan.reservations.service.FileStorageService;
import com.dunnnan.reservations.service.ReservationService;
import com.dunnnan.reservations.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PaginationConfig paginationConfig;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/home")
    public String home(
            Model model,
            @RequestParam(name = "sortField", required = false) String sortField,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size,
            @RequestParam(name = "types") Optional<List<String>> types,
            @RequestParam(name = "search") Optional<String> search
    ) {

        // Load Resource Page
        Page<Resource> resourcePage = resourceService.loadResourcePage(
                sortField, sortDirection, page, size, types, search);

        // Load Page Navigation parameters
        Map<String, Integer> pageParameters = resourceService.getPageNavigationParameters(page, resourcePage.getTotalPages());

        // Inject parameters into the model
        // Page
        model.addAttribute("startPage", pageParameters.get("startPage"));
        model.addAttribute("endPage", pageParameters.get("endPage"));
        model.addAttribute("currentPage", resourcePage.getNumber());
        model.addAttribute("totalPages", resourcePage.getTotalPages());
        model.addAttribute("totalElements", resourcePage.getTotalElements());

        // Sort
//        model.addAttribute("sortOptions", resourceService.getSortOptions());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);

        // Filter
        model.addAttribute("types", types.orElse(Collections.emptyList()));

        // Search
        model.addAttribute("search", search.orElse(null));

        // Types
        model.addAttribute("typeOptions", Arrays.asList(ResourceType.values()));

        // New Resource dto
        if (!model.containsAttribute("resource")) {
            model.addAttribute("resource", new ResourceDto());
        }

        // Data
        model.addAttribute("resources", resourcePage);
        return "home";
    }

    @PostMapping("/resource")
    public String addResource(
            @ModelAttribute("resource") @Validated ResourceDto resource,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException {

        // Validate sent image (if it's an image)
        if (!fileStorageService.isImage(resource.getImage())) {
            result.rejectValue("image", "error.image", "File is not an image");
        }

        // Validate sent type (if it's listed in ResourceType enum)
        if (Arrays.stream(ResourceType.values())
                .noneMatch(type -> type.name().equalsIgnoreCase(resource.getType()))
        ) {
            result.rejectValue("type", "error.type", "Type doesn't exist in system");
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.resource", result);
            redirectAttributes.addFlashAttribute("resource", resource);
            redirectAttributes.addFlashAttribute("showModal", true);
            return "redirect:/home";
        }

        // Save new resource
        resourceService.addResource(resource);

        return "redirect:/home";
    }

    @GetMapping("/resource/{id}")
    public String resource(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int weeksLater,
            Model model
    ) {
        Optional<Resource> resource = resourceService.getResourceById(id);
        if (resource.isPresent()) {
            model.addAttribute("resource", resource.get());
            model.addAttribute("reservation", new ReservationDto());

            try {
                model.addAttribute("calendarData", reservationService.getReservationCalendar(id, weeksLater));
            } catch (Exception e) {
                model.addAttribute("calendarData", null);
            }

            return "resource-detail";
        }
        return "redirect:/home";
    }

}
