package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Product;

import java.util.List;


public interface ProductService {
    Long insert(Product product);
    void deleteById(Long id);
    void deleteByIdList(List<Long> idList);
    Product findById(Long id);
    List<Product> findAll();
    List<Product> findByName(String name);
    Product update(Product product);
}
