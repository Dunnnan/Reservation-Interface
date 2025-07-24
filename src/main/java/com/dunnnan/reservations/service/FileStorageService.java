package com.dunnnan.reservations.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.dunnnan.reservations.constants.StorageConstants.RESOURCE_IMAGE_PATH;

@Service
public class FileStorageService {

    private static final Path storageFolder = Paths.get(RESOURCE_IMAGE_PATH).toAbsolutePath();

    public boolean isImage(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());
        return mimeType.startsWith("image/");
    }

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
