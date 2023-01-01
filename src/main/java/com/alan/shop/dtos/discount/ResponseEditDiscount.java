package com.alan.shop.dtos.discount;

import com.alan.shop.models.Discount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEditDiscount {
    private boolean success;
    private String message;
    private Discount discount;
}
