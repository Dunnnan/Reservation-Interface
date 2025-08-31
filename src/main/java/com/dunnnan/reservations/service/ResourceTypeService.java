package com.dunnnan.reservations.service;

import com.dunnnan.reservations.repository.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeService {

    @Autowired
    ResourceTypeRepository resourceTypeRepository;

}
