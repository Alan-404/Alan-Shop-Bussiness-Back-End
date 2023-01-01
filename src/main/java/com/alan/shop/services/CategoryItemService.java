package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.CategoryItem;

public interface CategoryItemService {
    public CategoryItem saveItem(CategoryItem item);
    public boolean deleteAllItems(String productId);
    public List<CategoryItem> getCategoriesItemByProduct(String productId);
}
