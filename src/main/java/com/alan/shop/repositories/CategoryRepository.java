package com.alan.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Category;
import com.alan.shop.utils.queries.CategoryQuery;


public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = CategoryQuery.paginationCategories, nativeQuery = true)
    Optional<List<Category>> paginationCategories(int num, int offset);
}
