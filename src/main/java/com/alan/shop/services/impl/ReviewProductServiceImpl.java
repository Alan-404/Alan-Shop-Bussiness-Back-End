package com.alan.shop.services.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.alan.shop.models.ReviewProduct;
import com.alan.shop.repositories.ReviewProductRepository;
import com.alan.shop.services.ReviewProductService;


@Service
public class ReviewProductServiceImpl implements ReviewProductService {
    private final ReviewProductRepository reviewProductRepository;

    public ReviewProductServiceImpl(ReviewProductRepository reviewProductRepository){
        this.reviewProductRepository = reviewProductRepository;
    }

    @Override
    public ReviewProduct saveReview(ReviewProduct review){
        try{
            review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            return this.reviewProductRepository.save(review);
        }
        catch(Exception exception){
            return null;
        }   
    }
}
