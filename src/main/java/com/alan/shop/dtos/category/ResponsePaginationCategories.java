package com.alan.shop.dtos.category;

import java.util.List;

import com.alan.shop.models.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePaginationCategories {
    private List<Category> categories;
    private int totalPage;
    private int totalCategories;
}
