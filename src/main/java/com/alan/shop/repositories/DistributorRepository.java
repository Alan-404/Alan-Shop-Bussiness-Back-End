package com.alan.shop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Distributor;
import com.alan.shop.utils.queries.DistributorQuery;

public interface DistributorRepository extends JpaRepository<Distributor, String> {
    @Query(value = DistributorQuery.queryGetDistributorByUserId, nativeQuery = true)
    public Optional<Distributor> getDistributorByUserId(String userId);
}
