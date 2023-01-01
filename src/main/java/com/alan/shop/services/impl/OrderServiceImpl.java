package com.alan.shop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Order;
import com.alan.shop.repositories.OrderRepository;
import com.alan.shop.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public Order saveOrder(Order order){
        return this.orderRepository.save(order);
    }

    @Override
    public boolean saveAllOrders(List<Order> orders){
        try{
            this.orderRepository.saveAll(orders);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> getOrdersByBill(String billId){
        try{
            Optional<List<Order>> orders = this.orderRepository.getOrdersByBill(billId);
            if (orders.isPresent() == false){
                return null;
            }

            return orders.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
