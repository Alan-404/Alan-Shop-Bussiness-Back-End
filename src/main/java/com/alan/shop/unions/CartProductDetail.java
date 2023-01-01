package com.alan.shop.unions;

import com.alan.shop.models.Cart;
import com.alan.shop.models.Discount;
import com.alan.shop.models.Product;
import com.alan.shop.models.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDetail {
    private Cart cart;
    private Product product;
    private Discount discount;
    private Warehouse warehouse;
}
