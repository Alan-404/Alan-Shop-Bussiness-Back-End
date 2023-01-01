package com.alan.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alan.shop.models.TypeProduct;

public interface TypeProductRepository extends JpaRepository<TypeProduct, String> {
    
}
