package com.alan.shop.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alan.shop.models.CategoryItem;
import com.alan.shop.repositories.CategoryItemRepository;
import com.alan.shop.services.CategoryItemService;
import com.google.common.base.Optional;

@Service
public class CategoryItemServiceImpl implements CategoryItemService {
    private final CategoryItemRepository categoryItemRepository;
    
    public CategoryItemServiceImpl(CategoryItemRepository categoryItemRepository){
        this.categoryItemRepository = categoryItemRepository;
    }

    @Override
    public CategoryItem saveItem(CategoryItem item){
        try{
            return this.categoryItemRepository.save(item);
        }
        catch(Exception exception){
            return null;
        }
    }

    @Override
    public boolean deleteAllItems(String productId){
        try{
            return true;
        }
        catch(Exception exception){
            return false;
        }
    }
    @Override
    public List<CategoryItem> getCategoriesItemByProduct(String productId){
        try{
            Optional<List<CategoryItem>> items = this.categoryItemRepository.getCategoriesItemByProduct(productId);
            if (items.isPresent() == false){
                return null;
            }

            return items.get();
        }   
        catch(Exception exception){
            return null;
        }
    }
}
