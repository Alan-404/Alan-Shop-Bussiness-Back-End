package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.dtos.reviewProduct.ReviewProductDTO;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Order;
import com.alan.shop.models.ReviewProduct;
import com.alan.shop.services.OrderService;
import com.alan.shop.services.ReviewProductService;

@RestController
@RequestMapping("/review")
public class ReviewProductController {
    private final ReviewProductService reviewProductService;
    private final JwtFilter jwtFilter;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    public ReviewProductController(ReviewProductService reviewProductService, OrderService orderService){
        this.reviewProductService = reviewProductService;
        this.jwtFilter = new JwtFilter();
        this.modelMapper = new ModelMapper();
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addReview(@RequestBody ReviewProductDTO data, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }

            ReviewProduct review = this.modelMapper.map(data, ReviewProduct.class);
            review.setUserId(userId);
            System.out.println(review);
            Order order = this.orderService.getOrderByBillAndProduct(data.getBillId(), data.getProductId());
            if (order == null){
                return ResponseEntity.status(400).body(new Response(false, "Failed"));
            }

            order.setReviewed(true);
            if (this.orderService.editOrder(order) == false){
                return ResponseEntity.status(400).body(new Response(false, "Failed"));
            }

            if (this.reviewProductService.saveReview(review) == null){
                return ResponseEntity.status(400).body(new Response(false, "Failed"));
            }

            return ResponseEntity.status(200).body(new Response(true, "Saved Review"));
            
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }
}
