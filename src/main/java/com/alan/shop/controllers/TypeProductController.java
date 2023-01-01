package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.middleware.RoleFilter;
import com.alan.shop.models.TypeProduct;
import com.alan.shop.services.AccountService;
import com.alan.shop.services.RoleService;
import com.alan.shop.services.TypeProductService;

@RestController
@RequestMapping("/type")
public class TypeProductController {
    private final TypeProductService typeProductService;
    private final JwtFilter jwtFilter;
    private final RoleFilter roleFilter;
    private final AccountService accountService;
    private final RoleService roleService;

    public TypeProductController(TypeProductService typeProductService, AccountService accountService, RoleService roleService){
        this.typeProductService = typeProductService;
        this.jwtFilter = new JwtFilter();
        this.accountService = accountService;
        this.roleService = roleService;
        this.roleFilter = new RoleFilter(this.accountService, this.roleService);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> saveTypeProduct(@RequestBody TypeProduct type, HttpServletRequest httpServletRequest){
        try{
            String userId =  this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }
            if (this.roleFilter.checkRoleAdmin(userId) == false){
                return ResponseEntity.status(400).body(new Response(false, "You are not allowed"));
            }

            this.typeProductService.saveType(type);
            return ResponseEntity.status(200).body(new Response(true, "Successfully"));

        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));

        }
    }
}
