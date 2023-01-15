package com.alan.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Order;
import com.alan.shop.utils.queries.OrderQuery;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = OrderQuery.queryOrdersByBill, nativeQuery = true)
    Optional<List<Order>> getOrdersByBill(String billId);

    @Query(value = OrderQuery.queryOrderByBillAndProduct, nativeQuery = true)
    Optional<Order> getOrderByBillAndProduct(String billId, String productId);
}
