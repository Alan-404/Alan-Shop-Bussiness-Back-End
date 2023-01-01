package com.alan.shop.utils.queries;

public class CartQuery {
    public static final String queryGetCartByProduct = "SELECT * FROM CARTS WHERE PRODUCT_ID = ?1 AND USER_ID = ?2";
    public static final String queryGetCartByUser = "SELECT * FROM CARTS WHERE USER_ID = ?1";
    public static final String queryGetCartToOrder = "SELECT * FROM CARTS WHERE USER_ID = ?1 AND STATUS = TRUE";
}
