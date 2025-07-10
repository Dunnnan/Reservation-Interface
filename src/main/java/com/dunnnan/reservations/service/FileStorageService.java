package com.dunnnan.reservations.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path storageFolder = Paths.get("src/main/resources/static/images").toAbsolutePath();

    public String saveImage(MultipartFile file) throws IOException {

        String originalImageName = file.getOriginalFilename();
        String baseName = "";
        String extension = "";

        if (originalImageName != null) {
            extension = originalImageName.substring(originalImageName.lastIndexOf('.'));
            baseName = originalImageName.substring(0, originalImageName.lastIndexOf('.'));
        }

        String uniqueImageName = baseName + "-" + UUID.randomUUID() + extension;

        File saveDestination = new File(storageFolder.toFile(), uniqueImageName);
        file.transferTo(saveDestination);

        return uniqueImageName;
    }

}
