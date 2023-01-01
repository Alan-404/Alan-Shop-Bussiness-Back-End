package com.alan.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alan.shop.models.Role;

public interface RoleReponsitory extends JpaRepository<Role, Integer> {
    
}
