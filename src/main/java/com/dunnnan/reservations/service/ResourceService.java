package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

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

}
