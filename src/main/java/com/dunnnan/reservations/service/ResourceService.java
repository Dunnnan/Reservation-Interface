package com.dunnnan.reservations.service;

import com.dunnnan.reservations.config.PaginationConfig;
import com.dunnnan.reservations.config.ReservationConfig;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.model.dto.ResourceDto;
import com.dunnnan.reservations.repository.ResourceRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private ReservationConfig reservationConfig;

    @Autowired
    private AvailabilityService availabilityService;

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

    public Sort getSort(String sortDirection, String sortField) {
        return Sort.by(
                Sort.Direction.fromString(sortDirection == null ||
                        sortDirection.isEmpty() ? "asc" : sortDirection),
                sortField == null || sortField.isEmpty() ? "id" : sortField
        );
    }

    public Pageable getPageable(Optional<Integer> page, Optional<Integer> size, Sort sort) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(paginationConfig.getDefaultPageSize());

        return PageRequest.of(currentPage, pageSize, sort);
    }

    public Page<Resource> getResourcePage(Pageable pageable, Optional<List<String>> types, Optional<String> search) {
        // All
        if (types.isEmpty() && search.isEmpty()) {
            return getAllResources(pageable);
        }
        // Search
        else if (types.isEmpty()) {
            return getResourcesByName(pageable, search.get());
        }
        // Filter
        else if (search.isEmpty()) {
            return getResourcesByTypeIn(pageable, types.get());
        }
        // Search & Filter
        else {
            return getResourcesByTypeAndName(pageable, types.get(), search.get());
        }
    }

    public Map<String, Integer> getPageNavigationParameters(
            Optional<Integer> page,
            int totalPages
    ) {

        // Handle Page parameter
        int currentPage = page.orElse(0);

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
        // Handle Page parameters
        Sort sort = getSort(sortDirection, sortField);
        Pageable pageable = getPageable(page, size, sort);

        return getResourcePage(pageable, types, search);
    }

    public String reverseDirection(String direction) {
        return "asc".equalsIgnoreCase(direction) ? "desc" : "asc";
    }

    public boolean isImage(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());
        return mimeType.startsWith("image/");
    }

    public void addResource(ResourceDto resourceDto) throws IOException {
        Resource resource = new Resource(
                resourceDto.getName(),
                resourceDto.getDescription(),
                fileStorageService.saveImage(resourceDto.getImage()),
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
