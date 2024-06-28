package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/car/search/id/{id}")
    Car getCarById(@PathVariable long id) {
        return carService.findById(id);
    }

    @GetMapping("/car/search/name/{name}")
    List<Car> getCarsByTransporter(@PathVariable String name) {
        return carService.findByTransporter(name);
    }

    @GetMapping("/car/search")
    List<Car> getAllCar()
    {
        return carService.findAll();
    }

    @PostMapping("/car/insert")
    Long insertCar(@RequestBody Car car)
    {
        return carService.insert(car);
    }

    @DeleteMapping("/car/delete/id/{id}")
    void deleteCarById(@PathVariable long id)
    {
        carService.deleteById(id);
    }

    @PutMapping("/car/update")
    Car updateCar(@RequestBody Car car)
    {
        return carService.update(car);
    }

}
