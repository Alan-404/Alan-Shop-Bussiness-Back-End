package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Distributor;
import com.alan.shop.repositories.DistributorRepository;
import com.alan.shop.services.DistributorService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;


@Service
public class DistributorServiceImpl implements DistributorService {
    private final DistributorRepository distributorRepository;
    private final Generator generator;

    public DistributorServiceImpl(DistributorRepository distributorRepository){
        this.distributorRepository = distributorRepository;
        this.generator = new Generator();
    }


    @Override
    public Distributor registerDistributor(Distributor distributor){
        distributor.setId(this.generator.generateId(Constants.lengthId));
        distributor.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        distributor.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        return this.distributorRepository.save(distributor);
    }

    @Override
    public Distributor getDistributorByUserId(String userId){
        Optional<Distributor> distributor = this.distributorRepository.getDistributorByUserId(userId);
        if (distributor.isPresent() == false){
            return null;
        }
        return distributor.get();
    }
}
