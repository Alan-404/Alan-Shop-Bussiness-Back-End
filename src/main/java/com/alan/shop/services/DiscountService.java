package com.alan.shop.services;

import com.alan.shop.models.Discount;

public interface DiscountService {
    public Discount saveDiscount(Discount discount);
    public Discount editDiscount(Discount discount);
    public Discount getDiscountById(int id);
    public Discount getDiscountByProductId(String productId);
}
