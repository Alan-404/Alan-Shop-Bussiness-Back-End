package com.alan.shop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.alan.shop.utils.queries.AccountQuery;
import com.alan.shop.models.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value = AccountQuery.queryAccountByUserId, nativeQuery = true)
    Optional<Account> getAccountByUserId(String userId);
    
    
}
