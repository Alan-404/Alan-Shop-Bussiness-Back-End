package com.alan.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Product;
import com.alan.shop.utils.queries.ProductQuery;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = ProductQuery.paginationProducts, nativeQuery = true)
    Optional<List<Product>> paginationProducts(int num, int offset);

    @Query(value = ProductQuery.paginationProductsOfDistributor, nativeQuery = true)
    Optional<List<Product>> paginationProductsByDistributor(String distributorId, int num, int offset);

    @Query(value = ProductQuery.getProductsOfDistributor, nativeQuery = true)
    Optional<List<Product>> getAllProductsOfDistributor(String distributorId);
}
