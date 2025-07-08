package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/home")
    public String home(
            Model model,
            @RequestParam(name = "sortField", required = false) String sortField,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size,
            @RequestParam(name = "type") Optional<List<String>> types,
            @RequestParam(name = "search") Optional<String> search
    ) {

        // Handle Page parameters
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(3);

        // Handle Sort parameters
        Sort sort = Sort.by(
                Sort.Direction.fromString(
                        Optional.ofNullable(sortDirection).orElse("asc")),
                Optional.ofNullable(sortField).orElse("id")
        );

        // Handle Resources loading
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        Page<Resource> resourcePage;
        // All
        if (types.isEmpty() && search.isEmpty()) {
            resourcePage = resourceService.getAllResources(pageable);
        }
        // Search
        else if (types.isEmpty()) {
            resourcePage = resourceService.getResourcesByName(pageable, search.get());
        }
        // Filter
        else if (search.isEmpty()) {
            resourcePage = resourceService.getResourcesByTypeIn(pageable, types.get());
        }
        // Search & Filter
        else {
            resourcePage = resourceService.getResourcesByTypeAndName(pageable, types.get(), search.get());
        }

        // Handle navigation bar parameters
        int startPage = Math.max(0, currentPage - 4);
        int endPage = Math.min(resourcePage.getTotalPages() - 1, currentPage + 5);

        // Inject parameters into the model
        // Page
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", resourcePage.getNumber());
        model.addAttribute("totalPages", resourcePage.getTotalPages());
        model.addAttribute("totalElements", resourcePage.getTotalElements());

        // Sort
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection != null && sortDirection.equals("asc") ? "desc" : "asc");

        // Filter
        model.addAttribute("types", types);

        // Search
        model.addAttribute("search", search);

        // Data
        model.addAttribute("resources", resourcePage);
        return "home";
    }

//    @PostMapping("/home")
//    public String addResource() {
//
//    }
//
//
//    @PostMapping("/home/add")

    @GetMapping("/resource/{id}")
    public String resource(
            @PathVariable("id") Long id,
            Model model
    ) {
        Optional<Resource> resource = resourceService.getResourceById(id);
        if (resource.isPresent()) {
            model.addAttribute("resource", resource.get());
            return "resource-detail";
        }
        return "redirect:/home";
    }

}
