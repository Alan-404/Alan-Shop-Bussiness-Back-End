package com.alan.shop.unions;

import com.alan.shop.models.Order;
import com.alan.shop.models.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoOrder {
    private Order order;
    private Product product;
}
