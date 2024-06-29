package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/search/id/{id}")
    Car getCarById(@PathVariable long id) {
        return carService.findById(id);
    }

    @GetMapping("/search/name/{name}")
    List<Car> getCarsByTransporter(@PathVariable String name) {
        return carService.findByTransporter(name);
    }

    @GetMapping("/search")
    List<Car> getAllCar()
    {
        return carService.findAll();
    }

    @PostMapping("/insert")
    Long insertCar(@RequestBody Car car)
    {
        return carService.insert(car);
    }

    @DeleteMapping("/delete/id/{id}")
    void deleteCarById(@PathVariable long id)
    {
        carService.deleteById(id);
    }

    @PutMapping("/update")
    Car updateCar(@RequestBody Car car)
    {
        return carService.update(car);
    }

}
