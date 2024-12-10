package com.example.inventory.repository;

import com.example.inventory.model.Part;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartRepository extends MongoRepository<Part, String> {
    Part findBySku(String sku);
}
