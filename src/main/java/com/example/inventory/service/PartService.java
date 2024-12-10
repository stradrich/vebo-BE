package com.example.inventory.service;

import com.example.inventory.model.Part;
import com.example.inventory.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    public Part getPartBySku(String sku) {
        return partRepository.findBySku(sku);
    }

    public Part createPart(Part part) {
        return partRepository.save(part);
    }
}
