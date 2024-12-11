package com.example.inventory.controller;

import com.example.inventory.model.Part;
import com.example.inventory.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/parts")
public class PartController {

    // Initialize the logger
    private static final Logger logger = LoggerFactory.getLogger(PartController.class);

    @Autowired
    private PartRepository partRepository;

    @PostMapping
    public Part createPart(@RequestBody Part part) {
        // Log the incoming request with the part payload
        logger.info("Received POST request to create part: {}", part);

        // Save the part to the database
        Part savedPart = partRepository.save(part);

        // Log the saved part
        logger.info("Part saved to database: {}", savedPart);

        return savedPart;
    }

    // Additional endpoints can be added here as needed
}
