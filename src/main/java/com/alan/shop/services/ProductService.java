package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.Product;



public interface ProductService {
    public Product saveProduct(Product product);
    public Product getProductById(String id);
    public Product editProduct(Product product);
    public List<Product> getAllProducts();
    public List<Product> paginationProducts(int num, int page);
    public List<Product> getProductsByDistributor(String distributorId);
    public List<Product> paginationProductsByDistributor(String distributorId, int page, int num);

}
