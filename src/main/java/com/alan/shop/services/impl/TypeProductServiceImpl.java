package com.alan.shop.services.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.alan.shop.models.TypeProduct;
import com.alan.shop.repositories.TypeProductRepository;
import com.alan.shop.services.TypeProductService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;

@Service
public class TypeProductServiceImpl implements TypeProductService {
    private final TypeProductRepository typeProductRepository;
    private final Generator generator;

    public TypeProductServiceImpl(TypeProductRepository typeProductRepository){
        this.typeProductRepository = typeProductRepository;
        this.generator = new Generator();
    }

    @Override
    public TypeProduct saveType(TypeProduct type){
        type.setId(this.generator.generateId(Constants.lengthId));
        type.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        type.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        return this.typeProductRepository.save(type);
    }
}
