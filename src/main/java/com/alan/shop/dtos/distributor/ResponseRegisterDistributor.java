package com.alan.shop.dtos.distributor;

import com.alan.shop.models.Distributor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRegisterDistributor {
    private boolean success = false;
    private String message = "";
    private Distributor distributor = null;

    public ResponseRegisterDistributor buildSuccess(boolean successs){
        this.success = successs;
        return this;
    }

    public ResponseRegisterDistributor buildMessage(String message){
        this.message = message;
        return this;
    }

    public ResponseRegisterDistributor buildDistributor(Distributor distributor){
        this.distributor = distributor;
        return this;
    }
}
