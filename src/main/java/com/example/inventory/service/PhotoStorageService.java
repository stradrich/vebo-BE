package com.example.inventory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class PhotoStorageService {

    @Value("${photo.upload-dir}") // Specify the upload directory in application.properties
    private String uploadDir;

    public void storePhoto(MultipartFile file, String partId) throws IOException {
        // Create a directory for the part if it doesn't exist
        Path partDir = Paths.get(uploadDir, partId);
        if (!Files.exists(partDir)) {
            Files.createDirectories(partDir);
        }

        // Save the file in the part-specific directory
        Path targetLocation = partDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    // public Resource loadPhoto(String partId, String fileName) throws IOException {
    //     Path filePath = Paths.get(uploadDir, partId, fileName);
    //     Resource resource = new UrlResource(filePath.toUri());
    //     if (resource.exists()) {
    //         return resource;
    //     } else {
    //         throw new IOException("Photo not found");
    //     }
    // }
}
