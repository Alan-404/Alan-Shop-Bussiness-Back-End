package com.alan.shop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Cart;
import com.alan.shop.repositories.CartRepository;
import com.alan.shop.services.CartService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;



@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final Generator generator;
    
    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
        this.generator = new Generator();
    }

    @Override
    public Cart saveCart(Cart cart){
        cart.setId(this.generator.generateId(Constants.lengthId));
        return this.cartRepository.save(cart);
    }

    @Override
    public Cart getCartByProduct(String productId, String userId){
        Optional<Cart> cart = this.cartRepository.getCartByProduct(productId, userId);
        if (cart.isPresent() == false){
            return null;
        }
        return cart.get();
    }

    @Override
    public Cart editCart(Cart cart){
        try{
            return this.cartRepository.save(cart);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteCart(String id){
        try{
            this.cartRepository.deleteById(id);
            return true;
        }
        catch(Exception exception){
            return false;
        }
    }

    @Override
    public List<Cart> getCartByUser(String userId){
        try{
            Optional<List<Cart>> cart = this.cartRepository.getCartByUser(userId);
            if (cart.isPresent() == false){
                return null;
            }

            return cart.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }


    @Override
    public Cart getCartById(String id){
        try{
            Optional<Cart> cart = this.cartRepository.findById(id);
            if (cart.isPresent() == false){
                return null;
            }
            return cart.get();
        }   
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateAllCarts(boolean status, String userId){
        try{
            Optional<List<Cart>> carts = this.cartRepository.getCartByUser(userId);
            if (carts.isPresent() == false){
                return false;
            }

            for (Cart cart : carts.get()) {
                cart.setStatus(status);
            }
            this.cartRepository.saveAll(carts.get());
            
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cart> getCartsToOrder(String userId){
        try{
            Optional<List<Cart>> carts = this.cartRepository.getCartToOrder(userId);
            if (carts.isPresent() == false){
                return null;
            }
            return carts.get();
        }
        catch(Exception exception){
            return null;
        }
    }

}
