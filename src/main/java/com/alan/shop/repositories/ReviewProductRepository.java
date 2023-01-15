package com.alan.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alan.shop.models.ReviewProduct;

public interface ReviewProductRepository extends JpaRepository<ReviewProduct, String> {
    
}
