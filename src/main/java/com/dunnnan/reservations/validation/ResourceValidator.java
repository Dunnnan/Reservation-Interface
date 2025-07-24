package com.dunnnan.reservations.validation;

import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ResourceValidator {

    @Autowired
    private ClassUtil classUtil;

    public boolean isCorrectSortDirection(String sortDirection) {
        return sortDirection.equalsIgnoreCase("asc") || sortDirection.equalsIgnoreCase("desc");
    }

    public String validateSortDirection(String sortDirection) {
        return (sortDirection == null || sortDirection.isEmpty() || !isCorrectSortDirection(sortDirection)) ? "asc" : sortDirection;
    }

    public String validateSortField(String sortField) {
        Set<String> validSortFields = classUtil.getValidFields(Resource.class);
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

}
