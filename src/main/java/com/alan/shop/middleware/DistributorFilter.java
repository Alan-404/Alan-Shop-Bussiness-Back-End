package com.alan.shop.middleware;

import com.alan.shop.models.Distributor;
import com.alan.shop.services.DistributorService;

public class DistributorFilter {
    private final DistributorService distributorService;

    public DistributorFilter(DistributorService distributorService){
        this.distributorService = distributorService;
    }

    public Distributor checkDistributor(String userId){
        try{
            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            return distributor;
        }
        catch(Exception exception){
            return null;
        }
    }
}
