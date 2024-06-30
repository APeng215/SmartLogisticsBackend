package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.CarSetoutRequest;
import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @Operation(summary = "通过ID获取车辆信息")
    @GetMapping("/search/id/{id}")
    public Car getCarById(@PathVariable long id) {
        return carService.findById(id);
    }

    @Operation(summary = "通过运输者姓名获取车辆列表")
    @GetMapping("/search/name/{name}")
    public List<Car> getCarsByTransporter(@PathVariable String name) {
        return carService.findByTransporter(name);
    }

    @Operation(summary = "获取所有车辆信息")
    @GetMapping("/search")
    public List<Car> getAllCar() {
        return carService.findAll();
    }

    @Operation(summary = "添加新车辆")
    @PostMapping("/insert")
    public Long insertCar(@RequestBody Car car) {
        return carService.insert(car);
    }

    @Operation(summary = "通过ID删除车辆")
    @DeleteMapping("/delete/id/{id}")
    public void deleteCarById(@PathVariable long id) {
        carService.deleteById(id);
    }

    @Operation(summary = "更新车辆信息")
    @PutMapping("/update")
    public Car updateCar(@RequestBody Car car) {
        return carService.update(car);
    }

    @Operation(summary = "车辆出发")
    @PutMapping("/setout")
    public Car move(@RequestBody CarSetoutRequest carSetoutRequest) {
        return carService.setout(carSetoutRequest);
    }

}
