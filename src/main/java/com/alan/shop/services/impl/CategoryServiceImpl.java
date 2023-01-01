package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Category;
import com.alan.shop.repositories.CategoryRepository;
import com.alan.shop.services.CategoryService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Generator generator;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
        this.generator = new Generator();
    }


    @Override
    public Category saveCategory(Category category){
        category.setId(this.generator.generateId(Constants.lengthId));
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        return this.categoryRepository.save(category);
    }

    @Override
    public boolean checkNameCategory(String name){
        List<Category> categories = this.categoryRepository.findAll();
        for (Category category : categories) {
            if (name.toLowerCase().equals(category.getName().toLowerCase())){
                return true;
            }
        }

        return false;
    }

    @Override
    public Category getCategoryById(String id){
        Optional<Category> category = this.categoryRepository.findById(id);

        if (category.isPresent() == false){
            return null;
        }

        return category.get();
    }

    @Override
    public Category editCategory(Category category){
        category.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        return this.categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories(){
        List<Category> categories = this.categoryRepository.findAll();
        return categories;
    }

    @Override
    public List<Category> paginationCategories(int num, int page){
        Optional<List<Category>> categories = this.categoryRepository.paginationCategories(num, (page-1)*num);
        return categories.get();
    }

    @Override
    public boolean deleteCategory(Category category){
        try{
            this.categoryRepository.delete(category);
            return true;
        }
        catch(Exception exception){
            return false;
        }
    }
}
