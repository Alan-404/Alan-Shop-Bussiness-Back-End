package com.alan.shop.dtos.reviewProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewProductDTO {
    private String productId;
    private String billId;
    private String content;
    private int star;
}
