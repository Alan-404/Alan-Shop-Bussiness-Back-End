package com.alan.shop.dtos.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductDTO {
    private String name;
    private String description;
    private double price;
    private List<MultipartFile> images;
    private List<String> categories;
}
