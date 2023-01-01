package com.alan.shop.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Warehouse;
import com.alan.shop.repositories.WarehouseRepository;
import com.alan.shop.services.WarehouseService;


@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository){
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse saveData(Warehouse data){
        return this.warehouseRepository.save(data);
    }

    @Override 
    public Warehouse getWarehouseById(int id){
        Optional<Warehouse> warehouse = this.warehouseRepository.findById(id);
        if (warehouse.isPresent() == false){
            return null;
        }
        return warehouse.get();
    }

    @Override
    public Warehouse changeWarehouse(Warehouse data, int quantity, boolean type){
        if (type == true){
            data.setQuantity(data.getQuantity() + quantity);
        }
        else{
            if (data.getQuantity() < quantity){
                return null;
            }
            data.setQuantity(data.getQuantity() - quantity);
        }
        return data;
    }

    @Override
    public Warehouse getWarehouseByProduct(String ProductId){
        Optional<Warehouse> data = this.warehouseRepository.getWarehouseByProduct(ProductId);
        if (data.isPresent() == false){
            return null;
        }

        return data.get();
    }

}
