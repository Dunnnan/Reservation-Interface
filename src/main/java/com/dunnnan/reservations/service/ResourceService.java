package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.model.dto.ResourceDto;
import com.dunnnan.reservations.repository.ResourceRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private FileStorageService fileStorageService;

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
    }

}
