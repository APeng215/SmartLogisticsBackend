package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.entity.Product;

import java.util.List;

public interface CarService {
    Long insert(Car car);
    void deleteById(Long id);
    Car findById(Long id);
    List<Car> findByTransporter(String name);

    List<Car> findAll();
    Car update(Car car);
}
