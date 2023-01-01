package com.alan.shop.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Role;
import com.alan.shop.repositories.RoleReponsitory;
import com.alan.shop.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleReponsitory roleReponsitory;

    public RoleServiceImpl(RoleReponsitory roleReponsitory){
        this.roleReponsitory = roleReponsitory;
    }

    @Override
    public Role getRoleById(int id){
        Optional<Role> role = this.roleReponsitory.findById(id);

        if (role.isPresent() == false){
            return null;
        }

        return role.get();
    }
}
