package com.example.inventory.controller;

import com.example.inventory.model.Part;
import com.example.inventory.service.PartService;
import com.example.inventory.service.PartService.PartNotFoundException;
import com.example.inventory.service.PhotoStorageService;

import jakarta.annotation.Resource;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/parts")
public class PartController {

    private static final Logger logger = LoggerFactory.getLogger(PartController.class);

    @Autowired
    private PartService partService;

    @PostMapping
    public Part createPart(@RequestBody Part part) {
        logger.info("Received POST request to create part: {}", part);
        return partService.savePart(part);  // Use PartService here
    }

    @GetMapping
    public List<Part> getAllParts() {
        logger.info("Received GET request to fetch all parts");
        return partService.findAll();  // Use PartService to fetch parts
    }

    @GetMapping("/{sku}")
    public Part getPartBySku(@PathVariable String sku) {
        logger.debug("Searching for part with SKU: {}", sku);
        Part part = partService.findBySku(sku);
        logger.debug("Found part: {}", part);
        return part;
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Part> updatePart(
            @PathVariable String sku,
            @RequestBody Part updatedPart) {
        Part updated = partService.updatePart(sku, updatedPart);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<String> deletePartBySku(@PathVariable String sku) {
    try {
        partService.deletePartBySku(sku);
        return ResponseEntity.ok("Part with SKU " + sku + " has been successfully deleted.");
    } catch (PartNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllParts() {
        partService.deleteAllParts();
        return ResponseEntity.ok("All parts have been successfully deleted.");
    }

    // PHOTO
    @Autowired
    private PhotoStorageService photoStorageService;

   // Handle photo upload
    @PostMapping("/{id}/uploadPhoto")
    public ResponseEntity<String> uploadPhoto(@PathVariable("id") String partId, @RequestParam("file") MultipartFile file) {
        try {
            // Use PhotoStorageService to handle file storage
            photoStorageService.storePhoto(file, partId);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo: " + e.getMessage());
        }
    }


   // Handle photo retrieval
//    @GetMapping("/{partId}/photos/{fileName}")
//    public ResponseEntity<?> getPhoto(@PathVariable String partId, @PathVariable String fileName) {
//        try {
//            return ResponseEntity.ok(photoStorageService.loadPhoto(partId, fileName));
//        } catch (IOException e) {
//            return ResponseEntity.status(404).body("Photo not found: " + e.getMessage());
//        }
//    }
}
