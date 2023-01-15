package com.alan.shop.utils.queries;

public class OrderQuery {
    public static final String queryOrdersByBill = "SELECT * FROM ORDERS WHERE BILL_ID = ?1";
    public static final String queryOrderByBillAndProduct = "SELECT * FROM ORDERS WHERE BILL_ID = ?1 AND PRODUCT_ID = ?2";
}
