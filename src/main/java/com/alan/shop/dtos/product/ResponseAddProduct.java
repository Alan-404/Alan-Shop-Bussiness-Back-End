package com.alan.shop.dtos.product;

import com.alan.shop.models.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAddProduct {
    private boolean success = false;
    private String message = "Fail to Save Product";
    private Product product = null;

    public ResponseAddProduct buildSuccess(boolean success){
        this.success = success;
        return this;
    }

    public ResponseAddProduct buildMessage(String message){
        this.message = message;
        return this;
    }

    public ResponseAddProduct buildProduct(Product product){
        this.product = product;
        return this;
    }
}
