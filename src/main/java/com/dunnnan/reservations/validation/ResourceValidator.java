package com.dunnnan.reservations.validation;

import com.dunnnan.reservations.constants.ResourceConstants;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.model.dto.ResourceDto;
import com.dunnnan.reservations.service.FileStorageService;
import com.dunnnan.reservations.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ResourceValidator {

    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    private ClassUtil classUtil;
    @Autowired
    private ResourceConstants resourceConstants;

    public boolean isCorrectSortDirection(String sortDirection) {
        return sortDirection.equalsIgnoreCase("asc") || sortDirection.equalsIgnoreCase("desc");
    }

    public String validateSortDirection(String sortDirection) {
        return (sortDirection == null || sortDirection.isEmpty() || !isCorrectSortDirection(sortDirection)) ? "asc" : sortDirection;
    }

    public boolean isUniqueSortFormula(String sortField) {
        return (resourceConstants.getSortOptions()
                .stream()
                .anyMatch(option -> option.equalsIgnoreCase(sortField))
        );
    }

    public String validateUniqueSortFormula(String sortField) {
        switch (sortField.toLowerCase()) {
            case "added":
                return "id";

            default:
                return "id";
        }
    }

    public String validateSortField(String sortField) {
        Set<String> validSortFields = classUtil.getValidFields(Resource.class);
        if (isUniqueSortFormula(sortField)) {
            return validateUniqueSortFormula(sortField);
        }
        return (sortField == null || sortField.isEmpty() || !validSortFields.contains(sortField)) ? "id" : sortField;
    }

    public List<String> validateTypesField(List<String> types) {
        Set<String> validTypes = Arrays.stream(ResourceType.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        return types.stream()
                .filter(validTypes::contains)
                .toList();
    }

    public void validateAddResourceDto(ResourceDto resourceDto, BindingResult result) throws IOException {
        // Validate sent image (if it's an image)
        if (!fileStorageService.isImage(resourceDto.getImage())) {
            result.rejectValue("image", "error.image", "File is not an image");
        }

        // Validate sent type (if it's listed in ResourceType enum)
        if (Arrays.stream(ResourceType.values())
                .noneMatch(type -> type.name().equalsIgnoreCase(resourceDto.getType()))
        ) {
            result.rejectValue("type", "error.type", "Type doesn't exist in system");
        }
    }

}
