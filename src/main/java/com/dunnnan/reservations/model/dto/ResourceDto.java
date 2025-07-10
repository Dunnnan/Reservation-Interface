package com.dunnnan.reservations.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ResourceDto {

    @NotBlank(message = "Name of a resource is required")
    private String name;

    @NotBlank(message = "Description of a resource is required")
    private String description;

    @NotNull(message = "Image of resource is required")
    private MultipartFile image;

    @NotBlank(message = "Type of resource is required")
    private String type;

    public ResourceDto() {
    }

    public @NotBlank(message = "Name of a resource is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name of a resource is required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Description of a resource is required") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description of a resource is required") String description) {
        this.description = description;
    }

    public @NotNull(message = "Image of resource is required") MultipartFile getImage() {
        return image;
    }

    public void setImage(@NotNull(message = "Image of resource is required") MultipartFile image) {
        this.image = image;
    }

    public @NotBlank(message = "Type of resource is required") String getType() {
        return type;
    }

    public void setType(@NotBlank(message = "Type of resource is required") String type) {
        this.type = type;
    }
}
