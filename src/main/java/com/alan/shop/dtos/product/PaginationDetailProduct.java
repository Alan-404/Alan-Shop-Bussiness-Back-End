package com.alan.shop.dtos.product;

import java.util.List;

import com.alan.shop.unions.InfoProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDetailProduct {
    private List<InfoProduct> products;
    private int totalPages;
    private int totalProducts;
}
