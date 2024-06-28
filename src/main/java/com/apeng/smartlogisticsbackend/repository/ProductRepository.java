package com.apeng.smartlogisticsbackend.repository;

import com.apeng.smartlogisticsbackend.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
