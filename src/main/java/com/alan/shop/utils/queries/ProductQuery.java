package com.alan.shop.utils.queries;

public class ProductQuery {
    public static final String paginationProducts = "SELECT * FROM PRODUCTS LIMIT ?1 OFFSET ?2";
    public static final String getProductsOfDistributor = "SELECT *  FROM PRODUCTS WHERE DISTRIBUTOR_ID = ?1";
    public static final String paginationProductsOfDistributor = "SELECT *  FROM PRODUCTS WHERE DISTRIBUTOR_ID = ?1 LIMIT ?2 OFFSET ?3";
}
