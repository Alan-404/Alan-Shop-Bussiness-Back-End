package com.alan.shop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Warehouse;
import com.alan.shop.utils.queries.WarehouseQuery;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    @Query(value = WarehouseQuery.queryGetWarehouseByProduct, nativeQuery = true)
    Optional<Warehouse> getWarehouseByProduct(String productId);
}
