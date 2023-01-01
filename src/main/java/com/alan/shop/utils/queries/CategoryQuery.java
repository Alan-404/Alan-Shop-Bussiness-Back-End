package com.alan.shop.utils.queries;

public class CategoryQuery {
    public static final String getCategoriesQuery = "SELECT * FROM CATEGORIES";
    public static final String paginationCategories = "SELECT * FROM CATEGORIES ORDER BY CREATED_AT DESC LIMIT ?1 OFFSET ?2";
}
