package com.alan.shop.unions;

import java.util.List;

import com.alan.shop.models.Bill;
import com.alan.shop.models.Order;
import com.alan.shop.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoBill {
    private Bill bill;
    private User user;
    private List<Order> orders;
}
