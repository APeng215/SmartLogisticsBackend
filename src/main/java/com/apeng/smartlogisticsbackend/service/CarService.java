package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.CarSetoutRequest;
import com.apeng.smartlogisticsbackend.entity.Car;

import java.util.List;

public interface CarService {
    Long insert(Car car);
    void deleteById(Long id);
    void deleteByIdList(List<Long> idList);
    Car findById(Long id);
    Car findByTransporter(String name);
    Car setout(CarSetoutRequest carSetoutRequest);
    List<Car> findAll();
    Car update(Car car);
    Car arrive(long carId);
}
