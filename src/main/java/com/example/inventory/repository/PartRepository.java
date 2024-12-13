package com.example.inventory.repository;

import com.example.inventory.model.Part;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartRepository extends MongoRepository<Part, String> {
    Optional<Part> findBySku(String sku);
}
