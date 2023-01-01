package com.alan.shop.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.User;
import com.alan.shop.utils.queries.UserQuery;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByEmail(String email);
    @Query(value = UserQuery.queryUserByEmail, nativeQuery = true)
    Optional<User> getUserByEmail(String email);
}
