package com.alan.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Bill;
import com.alan.shop.utils.queries.BillQuery;

public interface BillRepository extends JpaRepository<Bill, String> {
    @Query(value = BillQuery.queryGetBillsByUser, nativeQuery = true)
    Optional<List<Bill>> getBillsByUser(String userId);

    @Query(value = BillQuery.paginateBillsByUser, nativeQuery = true)
    Optional<List<Bill>> paginateBillsByUser(String userId, int num, int offset);
}
