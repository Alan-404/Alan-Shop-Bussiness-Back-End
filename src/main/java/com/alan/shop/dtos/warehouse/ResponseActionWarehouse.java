package com.alan.shop.dtos.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseActionWarehouse {
    private boolean success;
    private String message;

    public ResponseActionWarehouse buildSuccess(boolean success){
        this.success = success;
        return this;
    }

    public ResponseActionWarehouse buildMessgae(String message){
        this.message = message;
        return this;
    }
}
