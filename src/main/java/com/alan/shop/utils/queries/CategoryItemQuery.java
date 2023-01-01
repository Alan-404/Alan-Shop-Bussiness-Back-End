package com.alan.shop.utils.queries;

public class CategoryItemQuery {
    public static final String deleteAllItemsByProduct = "DELETE FROM CATEGORIES_ITEM WHERE PRODUCT_ID = ?1";
    public static final String getCategoriesItemByProduct = "SELECT * FROM CATEGORIES_ITEM WHERE PRODUCT_ID = ?1";
}
