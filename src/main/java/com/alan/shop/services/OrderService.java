package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.Order;

public interface OrderService {
    public Order saveOrder(Order order);
    public boolean saveAllOrders(List<Order> orders);
    public List<Order> getOrdersByBill(String billId);
}
