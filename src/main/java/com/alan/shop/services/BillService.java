package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.Bill;

public interface BillService {
    public Bill saveBill(Bill bill);
    public List<Bill> getBillsByUser(String userId);
    public List<Bill> paginateBillsByUser(String userId, int page, int num);
    public Bill getBillById(String billId);
}
