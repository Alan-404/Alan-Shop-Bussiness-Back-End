package com.alan.shop.dtos.register;

import com.alan.shop.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReponseRegisterDTO {
    private boolean success = false;
    private String message = "";
    private User user = null;
}
