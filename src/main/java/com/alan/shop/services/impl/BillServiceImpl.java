package com.alan.shop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Bill;
import com.alan.shop.repositories.BillRepository;
import com.alan.shop.services.BillService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;


@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final Generator generator;

    public BillServiceImpl(BillRepository billRepository){
        this.billRepository = billRepository;
        this.generator = new Generator();
    }

    @Override
    public Bill saveBill(Bill bill){
        bill.setId(this.generator.generateId(Constants.lengthId));
        return this.billRepository.save(bill);
    }

    @Override
    public List<Bill> getBillsByUser(String userId){
        try{
            Optional<List<Bill>> bills = this.billRepository.getBillsByUser(userId);
            if (bills.isPresent() == false){
                return null;
            }
            return bills.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Bill> paginateBillsByUser(String userId, int page, int num){
        try{
            Optional<List<Bill>> bills = this.billRepository.paginateBillsByUser(userId, num, (page - 1)*num);
            if (bills.isPresent() == false){
                return null;
            }

            return bills.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
