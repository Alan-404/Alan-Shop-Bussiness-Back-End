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
public class ResponseAddCategory {
    private boolean success = false;
    private String message = "";
    private Category category = null;

    public ResponseAddCategory buildSuccess(boolean success){
        this.success = success;
        return this;
    }

    public ResponseAddCategory buildMessage(String message){
        this.message = message;
        return this;
    }

    public ResponseAddCategory buildCategory(Category category){
        this.category = category;
        return this;
    }

}
