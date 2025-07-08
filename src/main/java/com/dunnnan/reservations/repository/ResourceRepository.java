package com.dunnnan.reservations.repository;

import com.dunnnan.reservations.model.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends
        JpaRepository<Resource, Long> {
    Page<Resource> findAllByTypeIn(Pageable pageable, List<String> types);

    Page<Resource> findAByNameContainingIgnoreCase(Pageable page, String search);

    Page<Resource> findByTypeInAndNameContainingIgnoreCase(Pageable page, List<String> types, String search);
}
