package com.alan.shop.services;

import com.alan.shop.models.Distributor;

public interface DistributorService {
    public Distributor registerDistributor(Distributor distributor);
    public Distributor getDistributorByUserId(String userId);
}
