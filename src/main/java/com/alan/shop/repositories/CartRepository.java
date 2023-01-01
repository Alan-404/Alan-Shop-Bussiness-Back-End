package com.alan.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Cart;
import com.alan.shop.utils.queries.CartQuery;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = CartQuery.queryGetCartByProduct, nativeQuery = true)
    Optional<Cart> getCartByProduct (String productId, String userId);

    @Query(value = CartQuery.queryGetCartByUser, nativeQuery = true)
    Optional<List<Cart>> getCartByUser (String userId);

    @Query(value = CartQuery.queryGetCartToOrder, nativeQuery = true)
    Optional<List<Cart>> getCartToOrder(String userId);
}
