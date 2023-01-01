package com.alan.shop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.discount.ResponseEditDiscount;
import com.alan.shop.models.Discount;
import com.alan.shop.services.DiscountService;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService){
        this.discountService = discountService;
    }

    @PutMapping("/edit")
    public ResponseEntity<ResponseEditDiscount> editDiscount(@RequestBody Discount discount){
        ResponseEditDiscount response = new ResponseEditDiscount();
        try{
            Discount checkDiscount = this.discountService.getDiscountByProductId(discount.getProductId());
            
            if (checkDiscount == null){
                response.setMessage("Not Found Discount");
                return ResponseEntity.status(404).body(response);
            }
            checkDiscount.setValue(discount.getValue());
            Discount updatedDiscount = this.discountService.editDiscount(checkDiscount);
            if (updatedDiscount == null){
                response.setMessage("Fail to Update");
                return ResponseEntity.status(400).body(response);
            }
            response.setDiscount(updatedDiscount);
            response.setSuccess(true);
            response.setMessage("Updated Discount");
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            System.out.println(exception);
            response.setMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

}
