package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Product;

import java.util.List;


public interface ProductService {
    Long insert(Product product);
    void deleteById(Long id);
    Product findById(Long id);
    List<Product> findAll();
    Product update(Product product);
}
