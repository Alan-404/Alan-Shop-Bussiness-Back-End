package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.Cart;

public interface CartService {
    public Cart saveCart(Cart cart);
    public Cart getCartByProduct(String productId, String userId);
    public Cart editCart(Cart cart);
    public boolean deleteCart(String id);
    public List<Cart> getCartByUser(String userId);
    public Cart getCartById(String id);
    public boolean updateAllCarts(boolean status, String userId);
    public List<Cart> getCartsToOrder(String userId);
}
