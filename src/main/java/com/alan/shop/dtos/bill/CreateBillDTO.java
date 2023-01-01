package com.alan.shop.dtos.bill;

import java.util.List;

import com.alan.shop.unions.CartProductDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillDTO {
    private List<CartProductDetail> products;
}
