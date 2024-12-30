package com.example.inventory.controller;

import com.example.inventory.model.Part;
import com.example.inventory.service.PartService;
import com.example.inventory.service.PartService.PartNotFoundException;
import com.example.inventory.service.PhotoStorageService;

import jakarta.annotation.Resource;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/parts")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from localhost:3000
public class PartController {

    private static final Logger logger = LoggerFactory.getLogger(PartController.class);
    
        private static final String UPLOAD_DIRECTORY = null;
    
        @Autowired
        private PartService partService;
    
        // @PostMapping
        // public Part createPart(@RequestBody Part part) {
        //     logger.info("Received POST request to create part: {}", part);
        
        //     // Ensure that sellingPrice is calculated if it's not provided
        //     if (part.getSellingPrice() == null || part.getSellingPrice() == 0.0) {
        //         part.calculateSellingPrice(); // Calculate sellingPrice if it's missing or 0
        //     }
        
        //     // Handle stock-related fields and minStockLevel if controlStock is YES
        //     part.handleStockFields();  // Enable stock-related fields if controlStock is YES
        
        //     // Log values before saving
        //     logger.info("Before save - stockLevel: {}, reservedStock: {}, availableStock: {}",
        //     part.getStockLevel(), part.getReservedStock(), part.getAvailableStock());
    
        //     // Save the part using the PartService
        //     return partService.savePart(part);
        // }
    
        @PostMapping
        public ResponseEntity<Object> createPart(@RequestBody Part part) {
            logger.info("Received POST request to create part: {}", part);
        
            // Check if a part with the same SKU already exists
            List<Part> existingParts = partService.findAll();
            for (Part existingPart : existingParts) {
                if (existingPart.getSku().equals(part.getSku())) {
                    logger.warn("SKU {} already exists. Cannot create new part.", part.getSku());
                    
                    // Custom error message in the response body
                    String errorMessage = "Part with SKU " + part.getSku() + " already exists.";
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                                         .body(errorMessage); // Send custom error message as a String
                }
            }
    
            // Handle stock-related fields and minStockLevel if controlStock is YES
            part.handleStockFields();  // Enable stock-related fields if controlStock is YES
    
            // If photoUrl is null, use a default image URL
            if (part.getPhotoUrl() == null || part.getPhotoUrl().isEmpty()) {
                part.setPhotoUrl("https://cdn1.npcdn.net/images/1628147456banner2.gif"); // Set default image URL
            }
        
            // Ensure that sellingPrice is calculated if it's not provided
            if (part.getSellingPrice() == null || part.getSellingPrice() == 0.0) {
                part.calculateSellingPrice(); // Calculate sellingPrice if it's missing or 0
            }
        
            // Handle stock-related fields and minStockLevel if controlStock is YES
            part.handleStockFields();  // Enable stock-related fields if controlStock is YES
        
            // Log values before saving
            logger.info("Before save - stockLevel: {}, reservedStock: {}, availableStock: {}",
            part.getStockLevel(), part.getReservedStock(), part.getAvailableStock());
        
            // Save the part using the PartService
            Part savedPart = partService.savePart(part);
        
            // Return the saved part as the response body
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPart); // Return ResponseEntity with Part object
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
      // Handle photo upload
        @PostMapping("/{sku}/uploadPhoto")
        public ResponseEntity<String> uploadPhoto(@PathVariable("sku") String sku, @RequestParam("file") MultipartFile file) throws IOException {
            // Use PhotoStorageService to handle file storage
            photoStorageService.storePhoto(file, sku);
            return ResponseEntity.ok("File uploaded successfully");
        }
    
        // Get latest photo
        @GetMapping("/uploads/photos/{folder}/latest")
        public ResponseEntity<String> getLatestFile(@PathVariable String folder) {
            File directory = new File(UPLOAD_DIRECTORY + folder);

        if (!directory.exists() || !directory.isDirectory()) {
            return ResponseEntity.badRequest().body("Folder not found or is not a directory");
        }

        File latestFile = Arrays.stream(directory.listFiles())
                .filter(File::isFile)
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);

        if (latestFile != null) {
            return ResponseEntity.ok(latestFile.getName());
        } else {
            return ResponseEntity.notFound().build();
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
