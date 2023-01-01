package com.alan.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private boolean success;
    private String message;
    public Response buildSuccess(boolean success){
        this.success = success;
        return this;
    }

    public Response buildMessage(String message){
        this.message = message;
        return this;
    }
}
