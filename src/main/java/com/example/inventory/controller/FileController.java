package com.example.inventory.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@RestController
public class FileController {

// GET UPLOADED PHOTO
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

// @CrossOrigin(origins = "http://localhost:3000") // Allow only requests from this origin
// // New functionality to fetch the latest file
// @GetMapping("/uploads/photos/{folder}/latest")
// public ResponseEntity<String> getLatestFile(@PathVariable String folder) {
//     File directory = new File(UPLOAD_DIRECTORY + folder);

//     if (!directory.exists() || !directory.isDirectory()) {
//         return ResponseEntity.badRequest().body("Folder not found or is not a directory");
//     }

//     File[] files = directory.listFiles();
//     if (files == null || files.length == 0) {
//         return ResponseEntity.notFound().build();
//     }

//     // Sort files by last modified date in descending order
//     Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

//     // Get the latest file's name
//     String latestFileName = files[0].getName();

//     return ResponseEntity.ok(latestFileName);
// }

@CrossOrigin(origins = "http://localhost:3000")  // Allow only requests from this origin
@GetMapping("/uploads/photos/{folder}/latest")
public ResponseEntity<Resource> getLatestFile(@PathVariable String folder) throws IOException {
    File directory = new File(UPLOAD_DIRECTORY + folder);

    if (!directory.exists() || !directory.isDirectory()) {
        return ResponseEntity.badRequest().body(null);
    }

    File[] files = directory.listFiles();
    if (files == null || files.length == 0) {
        return ResponseEntity.notFound().build();
    }

    // Sort files by last modified date in descending order
    Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

    // Get the latest file's name
    String latestFileName = files[0].getName();
    File latestFile = new File(UPLOAD_DIRECTORY + folder + "/" + latestFileName);

    if (latestFile.exists()) {
        Resource resource = new FileSystemResource(latestFile);

        // Automatically determine the content type based on file extension
        String contentType = Files.probeContentType(latestFile.toPath());
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
