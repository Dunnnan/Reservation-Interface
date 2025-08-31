package com.dunnnan.reservations.repository;

import com.dunnnan.reservations.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, String> {
    Optional<ResourceType> findById(Long id);

}
