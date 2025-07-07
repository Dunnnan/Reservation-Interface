package com.dunnnan.reservations.repository;

import com.dunnnan.reservations.model.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends
        JpaRepository<Resource, Long> {
    Page<Resource> findAllByType(Pageable pageable, String type);
}
