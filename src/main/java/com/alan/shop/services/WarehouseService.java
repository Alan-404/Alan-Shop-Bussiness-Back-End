package com.alan.shop.services;

import com.alan.shop.models.Warehouse;

public interface WarehouseService {
    public Warehouse saveData(Warehouse data);
    public Warehouse getWarehouseById(int id);
    public Warehouse changeWarehouse(Warehouse data, int quantity, boolean type);
    public Warehouse getWarehouseByProduct(String productId);
}
