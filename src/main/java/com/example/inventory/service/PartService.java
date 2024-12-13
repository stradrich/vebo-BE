package com.example.inventory.service;

import com.example.inventory.model.Part;
import com.example.inventory.repository.PartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    private final PartRepository partRepository;

    @Autowired
    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part savePart(Part part) {
        return partRepository.save(part);
    }
    
    public List<Part> findAll() {
        return partRepository.findAll();
    }

    public Part findBySku(String sku) {
        Optional<Part> optionalPart = partRepository.findBySku(sku);
        return optionalPart.orElseThrow(() -> new PartNotFoundException("Part with SKU " + sku + " not found"));
    }

    public class PartNotFoundException extends RuntimeException {
        public PartNotFoundException(String message) {
            super(message);
        }
    }


    public Part updatePart(String sku, Part updatedPart) {
        return partRepository.findBySku(sku)
            .map(existingPart -> {
            // Update all fields except immutable ones like `id` and `sku`
            // existingPart.setUuid(updatedPart.getUuid());
            existingPart.setStatus(updatedPart.getStatus());
            existingPart.setDescription(updatedPart.getDescription());
            existingPart.setSupplier(updatedPart.getSupplier());
            existingPart.setCostPrice(updatedPart.getCostPrice());
            existingPart.setSellingPrice(updatedPart.getSellingPrice());
            existingPart.setStockLevel(updatedPart.getStockLevel());
            existingPart.setReservedStock(updatedPart.getReservedStock());
            existingPart.setAvailableStock(updatedPart.getAvailableStock());
            existingPart.setPhotoUrl(updatedPart.getPhotoUrl());
            existingPart.setPartType(updatedPart.getPartType());
            existingPart.setMinStockLevel(updatedPart.getMinStockLevel());
            existingPart.setArchived(updatedPart.isArchived());
            existingPart.setControlled(updatedPart.isControlled());
            existingPart.setControlStock(updatedPart.isControlStock());

            // Save and return the updated part
            return partRepository.save(existingPart);
            })
            .orElseThrow(() -> new PartNotFoundException("Part with SKU " + sku + " not found"));
    }

    public void deletePartBySku(String sku) {
        Part part = partRepository.findBySku(sku)
            .orElseThrow(() -> new PartNotFoundException("Part with SKU " + sku + " not found"));
        partRepository.delete(part);
    }

    public void deleteAllParts() {
        partRepository.deleteAll();
    }
    
    
}
