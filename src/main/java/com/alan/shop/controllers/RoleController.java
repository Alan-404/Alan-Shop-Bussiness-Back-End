package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Account;
import com.alan.shop.services.AccountService;
import com.alan.shop.services.RoleService;


@RestController
@RequestMapping("/role")
public class RoleController {
    private final JwtFilter jwtFilter;
    private final AccountService accountService;

    public RoleController(RoleService roleService,  AccountService accountService){
        this.accountService = accountService;
        this.jwtFilter = new JwtFilter();
    }

    @GetMapping("")
    public ResponseEntity<Boolean> getRole(HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(false);
            }
            Account account = this.accountService.getAccountByUser(userId);
            if (account.getRole() == 1){
                return ResponseEntity.status(200).body(true);
            }
            return ResponseEntity.status(400).body(false);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(false);
        }
    }
}
