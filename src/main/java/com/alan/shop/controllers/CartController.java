package com.alan.shop.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.dtos.cart.UpdateAllCartStatusDTO;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Cart;
import com.alan.shop.models.Discount;
import com.alan.shop.models.Distributor;
import com.alan.shop.models.Product;
import com.alan.shop.models.Warehouse;
import com.alan.shop.services.CartService;
import com.alan.shop.services.DiscountService;
import com.alan.shop.services.DistributorService;
import com.alan.shop.services.ProductService;
import com.alan.shop.services.WarehouseService;
import com.alan.shop.unions.CartProductDetail;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final JwtFilter jwtFilter;
    private final ProductService productService;
    private final DistributorService distributorService;
    private final WarehouseService warehouseService;
    private final DiscountService discountService;

    public CartController(CartService cartService, ProductService productService, DistributorService distributorService, WarehouseService warehouseService, DiscountService discountService){
        this.cartService = cartService;
        this.jwtFilter = new JwtFilter();
        this.productService = productService;
        this.distributorService = distributorService;
        this.warehouseService = warehouseService;
        this.discountService = discountService;
    }

    @PostMapping("/handle")
    public ResponseEntity<Response> handleCart(@RequestBody Cart data, HttpServletRequest httpServletRequest){
        Response response = new Response();
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessage("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }

            if (data.getProductId() == null){
                response.buildMessage("Unknow Product");
                return ResponseEntity.status(400).body(response);
            }

            Product product = this.productService.getProductById(data.getProductId());
            if (product == null || product.isStatus() == false){
                response.buildMessage("Not Found Product");
                return ResponseEntity.status(404).body(response);
            }

            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            if (distributor != null && distributor.getId().equals(product.getDistributorId())){
                response.buildMessage("You are not allowed");
                return ResponseEntity.status(403).body(response);
            }

            Warehouse warehouse = this.warehouseService.getWarehouseByProduct(product.getId());
            if (warehouse == null || warehouse.getQuantity() < data.getQuantity()){
                response.buildMessage("Out of Stock");
                return ResponseEntity.status(400).body(response);
            }

            Cart cart = this.cartService.getCartByProduct(product.getId(), userId);
            if (cart == null){
                this.cartService.saveCart(data);
            }
            else{
                this.cartService.editCart(data);
            }
            response.buildMessage("Successfully").buildSuccess(true);

            return ResponseEntity.status(200).body(response);

        }
        catch(Exception exception){
            exception.printStackTrace();
            response.buildMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addToCart(@RequestBody Cart data, HttpServletRequest httpServletRequest){
        Response response = new Response();
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessage("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }

            if (data.getProductId() == null){
                response.buildMessage("Unknow Product");
                return ResponseEntity.status(400).body(response);
            }

            Product product = this.productService.getProductById(data.getProductId());
            if (product == null || product.isStatus() == false){
                response.buildMessage("Not Found Product");
                return ResponseEntity.status(404).body(response);
            }

            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            if (distributor != null && distributor.getId().equals(product.getDistributorId())){
                response.buildMessage("You are not allowed");
                return ResponseEntity.status(403).body(response);
            }
            Cart cart = new Cart();
            cart.setProductId(product.getId());
            cart.setUserId(userId);
            Cart productCart = this.cartService.getCartByProduct(product.getId(), userId);
            if (productCart == null){
                cart.setQuantity(data.getQuantity());
                this.cartService.saveCart(cart);
            }
            else{
                productCart.setQuantity(data.getQuantity() + productCart.getQuantity());
                this.cartService.editCart(productCart);
            }
            response.buildMessage("Add Cart Successfully").buildSuccess(true);

            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            response.buildMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/num")
    public ResponseEntity<Integer> getNumProductInCart(HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }
            List<Cart> cart = this.cartService.getCartByUser(userId);

            int count = 0;
            for (Cart item : cart) {
                count += item.getQuantity();
            }

            return ResponseEntity.status(200).body(count);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<CartProductDetail>> getMyCart(HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }
            List<Cart> carts = this.cartService.getCartByUser(userId);
            List<CartProductDetail> products = new ArrayList<>();
            for (int i =0 ;i<carts.size(); i++){
                CartProductDetail item = new CartProductDetail();
                item.setCart(carts.get(i));
                Product product = this.productService.getProductById(carts.get(i).getProductId());
                item.setProduct(product);
                Discount discount = this.discountService.getDiscountByProductId(carts.get(i).getProductId());
                item.setDiscount(discount);
                Warehouse warehouse = this.warehouseService.getWarehouseByProduct(carts.get(i).getProductId());
                item.setWarehouse(warehouse);
                products.add(item);

            }
            return ResponseEntity.status(200).body(products);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/order")
    public ResponseEntity<List<CartProductDetail>> getCartsToOrder(HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }
            List<Cart> carts = this.cartService.getCartsToOrder(userId);
            List<CartProductDetail> products = new ArrayList<>();
            for (Cart cart : carts) {
                CartProductDetail item = new CartProductDetail();
                item.setCart(cart);
                Product product = this.productService.getProductById(cart.getProductId());
                Discount discount = this.discountService.getDiscountByProductId(cart.getProductId());
                item.setProduct(product);
                item.setDiscount(discount);
                Warehouse warehouse = this.warehouseService.getWarehouseByProduct(cart.getProductId());
                item.setWarehouse(warehouse);
                products.add(item);
            }

            return ResponseEntity.status(200).body(products);
        }

        catch(Exception exception){
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCartItem(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        try{
            if (id == null){
                return ResponseEntity.status(404).body(new Response(false, "Not Found"));
            }
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }  

            Cart cart = this.cartService.getCartById(id);
            if (cart == null || cart.getUserId().equals(userId) == false){
                return ResponseEntity.status(403).body(new Response(false, "Not Allowed"));
            }

            this.cartService.deleteCart(id);

            return ResponseEntity.status(200).body(new Response(true, "Success"));
        }   
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }

    @PutMapping("/status")
    public ResponseEntity<Response> updateAllStatus(@RequestBody UpdateAllCartStatusDTO data, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }

            this.cartService.updateAllCarts(data.isStatus(), userId);
            return ResponseEntity.status(200).body(new Response(true, "Success"));
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }

}
