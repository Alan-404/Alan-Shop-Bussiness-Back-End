package com.alan.shop.utils.queries;

public class DiscountQuery {
    public static final String updateDiscountValue = "UPDATE DISCOUNT SET VALUE = ?2 AND MODIFIED_AT = DATE.NOW() WHERE ID = ?1";
    public static final String queryGetDiscountByProductId = "SELECT * FROM DISCOUNT WHERE PRODUCT_ID = ?1";
}
