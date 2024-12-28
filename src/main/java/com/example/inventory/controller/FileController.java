package com.example.inventory.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class FileController {

    private static final String UPLOAD_DIRECTORY = "/Users/drichintoshed/Desktop/interview/veboApp/veboBE/inventory/uploads/photos/";
  @GetMapping("/uploads/photos/{folder}/{filename}")
public ResponseEntity<Resource> getFile(@PathVariable String folder, @PathVariable String filename) throws IOException {
    File file = new File(UPLOAD_DIRECTORY + folder + "/" + filename);

    if (file.exists()) {
        Resource resource = new FileSystemResource(file);

        // Automatically determine the content type based on file extension
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";  // fallback type
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    } else {
        return ResponseEntity.notFound().build();
    }
}

    
}
