package com.alan.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alan.shop.models.HistoryWarehouse;

public interface HistoryWarehouseRepository extends JpaRepository<HistoryWarehouse, Integer> {
    
}
