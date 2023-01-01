package com.alan.shop.dtos.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionWarehouseDTO {
    private String productId;
    private boolean type;
    private int quantity;
}
