package com.dunnnan.reservations.service;

import com.dunnnan.reservations.config.PaginationConfig;
import com.dunnnan.reservations.constants.ReservationConstants;
import com.dunnnan.reservations.constants.ResourceConstants;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.model.dto.ResourceDto;
import com.dunnnan.reservations.repository.ResourceRepository;
import com.dunnnan.reservations.validation.ResourceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PaginationConfig paginationConfig;

    @Autowired
    private ReservationConstants reservationConstants;

    @Autowired
    private ResourceConstants resourceConstants;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private ResourceValidator resourceValidator;

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Page<Resource> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }

    public Optional<Resource> getResourceById(Long id) {
        return resourceRepository.findById(id);
    }

    public Page<Resource> getResourcesByTypeIn(Pageable pageable, List<String> types) {
        return resourceRepository.findAllByTypeIn(pageable, types);
    }

    public Page<Resource> getResourcesByName(Pageable page, String search) {
        return resourceRepository.findAByNameContainingIgnoreCase(page, search);
    }

    public Page<Resource> getResourcesByTypeAndName(Pageable page, List<String> types, String search) {
        return resourceRepository.findByTypeInAndNameContainingIgnoreCase(page, types, search);
    }

//    public ResourceDto getResoureDto(Long id) {
//        Optional<Resource> resourceOptional = getResourceById(id);
//        Resource resource = resourceOptional.orElse(new Resource());
//
//        return new ResourceDto(
//                resource.getName(),
//                resource.getDescription(),
//                resource.getImageName(),
//                resource.getType()
//        );
//    }

    public List<String> getSortOptions() {
        return resourceConstants.getSortOptions();
    }

    public Sort getSort(String sortDirection, String sortField) {
        sortField = resourceValidator.validateSortField(sortField);
        sortDirection = resourceValidator.validateSortDirection(sortDirection);

        return Sort.by(
                Sort.Direction.fromString(sortDirection),
                sortField
        );
    }

    public Pageable getPageable(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    public Page<Resource> getResourcePage(Pageable pageable, Optional<List<String>> types, Optional<String> search) {
        List<String> resourceTypes = resourceValidator.validateTypesField(types.orElse(new ArrayList<>()));

        // All
        if (resourceTypes.isEmpty() && search.isEmpty()) {
            return getAllResources(pageable);
        }
        // Search
        else if (resourceTypes.isEmpty()) {
            return getResourcesByName(pageable, search.get());
        }
        // Filter
        else if (search.isEmpty()) {
            return getResourcesByTypeIn(pageable, resourceTypes);
        }
        // Search & Filter
        else {
            return getResourcesByTypeAndName(pageable, resourceTypes, search.get());
        }
    }

    public Map<String, Integer> getPageNavigationParameters(
            Optional<Integer> page,
            int totalPages
    ) {

        // Handle Page parameter
        int currentPage = page.filter(p -> p >= 0).orElse(0);
        currentPage = Math.min(currentPage, totalPages - 1);

        // Handle navigation parameters
        int startPage = 0;
        int endPage = 0;

        if (totalPages > 0) {
            startPage = Math.max(0, currentPage - 4);
            endPage = Math.min(totalPages - 1, currentPage + 5);
        }

        return Map.of(
                "startPage", startPage,
                "endPage", endPage
        );
    }

    public Page<Resource> loadResourcePage(
            String sortField,
            String sortDirection,
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<List<String>> types,
            Optional<String> search
    ) {

        int requestedPage = page.filter(p -> p >= 0).orElse(0);
        int requestedSize = Math.min(size.orElse(paginationConfig.getDefaultPageSize()), paginationConfig.getMaxPageSize());

        // Handle Page parameters
        Sort sort = getSort(sortDirection, sortField);
        Pageable pageable = getPageable(requestedPage, requestedSize, sort);
        Page<Resource> resourcePage = getResourcePage(pageable, types, search);

        // Handle edge cases
        int totalPages = Math.max(resourcePage.getTotalPages() - 1, 0);
        if (requestedPage > totalPages) {
            pageable = getPageable(totalPages, requestedSize, sort);
            return getResourcePage(pageable, types, search);
        }

        return resourcePage;
    }

    public void addResource(ResourceDto resourceDto) throws IOException {
        String imageName = fileStorageService.saveImage(resourceDto.getImage());

        Resource resource = new Resource(
                resourceDto.getName(),
                resourceDto.getDescription(),
                imageName,
                ResourceType.valueOf(resourceDto.getType())
        );
        resourceRepository.save(resource);
        availabilityService.createDefaultAvailabilities(resource);
    }

    // Find resource by id
    public Resource findResourceById(Long id) {
        return resourceRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Resource with id: " + id + " was not found")
        );
    }

}
