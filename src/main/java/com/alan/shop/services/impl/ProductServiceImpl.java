package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Product;
import com.alan.shop.repositories.ProductRepository;
import com.alan.shop.services.ProductService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Generator generator;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.generator = new Generator();
    }

    @Override
    public Product saveProduct(Product product) {
        product.setId(this.generator.generateId(Constants.lengthId));
        product.setStatus(true);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        return this.productRepository.save(product);
    }

    @Override
    public Product getProductById(String id) {
        try {
            Optional<Product> product = this.productRepository.findById(id);
            if (product.isPresent() == false) {
                return null;
            }
            return product.get();
        } 
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public Product editProduct(Product product) {
        try{
            product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.productRepository.save(product);
        }   
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> paginationProducts(int num, int page) {
        try {
            Optional<List<Product>> products = this.productRepository.paginationProducts(num, (page - 1) * num);
            return products.get();
        } 
        catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    @Override
    public List<Product> paginationProductsByDistributor(String distributorId, int page, int num){
        try{
            Optional<List<Product>> products = this.productRepository.paginationProductsByDistributor(distributorId, num, (page - 1)*num);
            if (products.isPresent() == false){
                return null;
            }
            return products.get();
        }
        catch(Exception exception){
            return null;
        }
    }

    @Override
    public List<Product> getProductsByDistributor(String distributorId){
        try{
            Optional<List<Product>> products = this.productRepository.getAllProductsOfDistributor(distributorId);
            if (products.isPresent() == false){
                return null;
            }

            return products.get();
        }
        catch(Exception exception){
            return null;
        }
    }

}
