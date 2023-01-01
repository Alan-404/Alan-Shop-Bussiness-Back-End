package com.alan.shop.dtos.category;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryDTO {
    private String id;
    private String name;
    private MultipartFile file;
}
