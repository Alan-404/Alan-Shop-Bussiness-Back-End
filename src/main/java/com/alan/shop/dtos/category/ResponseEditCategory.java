package com.alan.shop.dtos.category;

import com.alan.shop.models.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEditCategory {
    private boolean success = false;
    private String message = "Fail to Edit Category";
    private Category category = null;

    public ResponseEditCategory buildSuccess(boolean success){
        this.success = success;
        return this;
    }

    public ResponseEditCategory buildMessage(String message){
        this.message = message;
        return this;
    }

    public ResponseEditCategory buildCategory(Category category){
        this.category = category;
        return this;
    }
}
