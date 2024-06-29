package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository repository;

    @Override
    public Long insert(Car car) {
        return repository.save(car).getId();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Car findById(Long id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Car> findByTransporter(String name) {
        return null;
    }

    @Override
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public Car update(Car car) {
        if (car.getId() == null) return null;
        return repository.save(car);
    }
}
