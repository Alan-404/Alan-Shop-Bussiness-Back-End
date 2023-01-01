package com.alan.shop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.CategoryItem;
import com.alan.shop.utils.queries.CategoryItemQuery;
import com.google.common.base.Optional;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Integer> {
    @Modifying
    @Query(value = CategoryItemQuery.deleteAllItemsByProduct, nativeQuery = true)
    void deleteAllItemsByProductId(String productId);

    @Query(value = CategoryItemQuery.getCategoriesItemByProduct, nativeQuery = true)
    Optional<List<CategoryItem>> getCategoriesItemByProduct(String productId);
}
