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

    @GetMapping("/car/{id}")
    Car getCarById(@PathVariable long id) {
        return carService.findById(id);
    }

    @GetMapping("/car")
    List<Car> getAllCar()
    {
        return carService.findAll();
    }

    @PostMapping("/car")
    Long insertCar(@RequestBody Car car)
    {
        return carService.insert(car);
    }

    @DeleteMapping("/car/{id}")
    void deleteCarById(@PathVariable long id)
    {
        carService.deleteById(id);
    }

    @PutMapping("/car")
    Car updateCar(@RequestBody Car car)
    {
        return carService.update(car);
    }

}
