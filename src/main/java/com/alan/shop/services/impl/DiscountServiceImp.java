package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Discount;
import com.alan.shop.repositories.DiscountRepository;
import com.alan.shop.services.DiscountService;


@Service
public class DiscountServiceImp implements DiscountService {
    public DiscountRepository discountRepository;

    public DiscountServiceImp(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount saveDiscount(Discount discount){
        discount.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        
        return this.discountRepository.save(discount);
    }

    @Override
    public Discount editDiscount(Discount discount){
        discount.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        return this.discountRepository.save(discount);
    }

    @Override
    public Discount getDiscountById(int id){
        Optional<Discount> discount = this.discountRepository.findById(id);
        if (discount.isPresent() == false){
            return null;
        }
        return discount.get();
    }

    @Override
    public Discount getDiscountByProductId(String productId){
        Optional<Discount> discount = this.discountRepository.getDiscountByProductId(productId);
        if (discount.isPresent() == false){
            return null;
        }

        return discount.get();
    }

}
