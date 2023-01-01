package com.alan.shop.utils.queries;

public class WarehouseQuery {
    public static final String queryGetWarehouseByProduct = "SELECT * FROM WAREHOUSE WHERE PRODUCT_ID = ?1";
}
