package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.Category;

public interface CategoryService {
    public Category saveCategory(Category category);
    public boolean checkNameCategory(String name);
    public Category getCategoryById(String id);
    public Category editCategory(Category category);
    public List<Category> getAllCategories();
    public List<Category> paginationCategories(int num, int page);
    public boolean deleteCategory(Category category);
}
