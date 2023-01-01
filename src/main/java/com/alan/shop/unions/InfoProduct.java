package com.alan.shop.unions;

import java.util.List;

import com.alan.shop.models.Category;
import com.alan.shop.models.Discount;
import com.alan.shop.models.Product;
import com.alan.shop.models.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoProduct {
    private Product product;
    private Discount discount;
    private Warehouse warehouse;
    private List<Category> categories;
    private int numMedia;
}
