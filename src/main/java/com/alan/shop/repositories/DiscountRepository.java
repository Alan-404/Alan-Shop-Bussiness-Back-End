package com.alan.shop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Discount;
import com.alan.shop.utils.queries.DiscountQuery;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Modifying
    @Query(value = DiscountQuery.updateDiscountValue, nativeQuery = true)
    public Optional<Discount> updateDiscountValue(int id, double value);

    @Query(value = DiscountQuery.queryGetDiscountByProductId, nativeQuery = true)
    public Optional<Discount> getDiscountByProductId(String productId);
}
