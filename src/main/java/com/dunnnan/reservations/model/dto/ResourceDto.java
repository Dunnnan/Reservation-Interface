package com.dunnnan.reservations.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class ResourceDto {

    @NotBlank(message = "Name of a resource is required")
    private String name;

    @NotBlank(message = "Description of a resource is required")
    private String description;

    @NotBlank(message = "Image of resource is required")
    private MultipartFile image;

    @NotBlank(message = "Type of resource is required")
    private String type;

}
