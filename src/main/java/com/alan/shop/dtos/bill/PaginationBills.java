package com.alan.shop.dtos.bill;

import java.util.List;

import com.alan.shop.unions.InfoBill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationBills {
    private List<InfoBill> bills;
    private int totalPages;
    private int totalBills;
}
