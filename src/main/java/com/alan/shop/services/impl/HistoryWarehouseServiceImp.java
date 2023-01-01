package com.alan.shop.services.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.alan.shop.models.HistoryWarehouse;
import com.alan.shop.repositories.HistoryWarehouseRepository;
import com.alan.shop.services.HistoryWarehouseService;

@Service
public class HistoryWarehouseServiceImp implements HistoryWarehouseService {
    private final HistoryWarehouseRepository historyWarehouseRepository;

    public HistoryWarehouseServiceImp(HistoryWarehouseRepository historyWarehouseRepository){
        this.historyWarehouseRepository = historyWarehouseRepository;
    }

    @Override
    public HistoryWarehouse saveHistory(HistoryWarehouse history){
        history.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return this.historyWarehouseRepository.save(history);
    }
    
}
