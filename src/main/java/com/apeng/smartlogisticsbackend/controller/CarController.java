package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.CarInsertRequest;
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
    public Car getCarsByTransporter(@PathVariable String name) {
        return carService.findByTransporter(name);
    }

    @Operation(summary = "获取所有车辆信息")
    @GetMapping("/search")
    public List<Car> getAllCar() {
        return carService.findAll();
    }

    @Operation(summary = "添加新车辆")
    @PostMapping("/insert")
    public Car insertCar(@RequestBody CarInsertRequest car) {
        return carService.insert(car);
    }

    @Operation(summary = "通过ID删除车辆")
    @DeleteMapping("/delete/id/{id}")
    public void deleteCarById(@PathVariable long id) {
        carService.deleteById(id);
    }

    @Operation(summary = "通过车辆的ID列表删除车辆")
    @DeleteMapping("/delete/idList")
    public void deleteCarByIdList(@RequestBody List<Long> idList){
        carService.deleteByIdList(idList);
    }

    @Operation(summary = "更新车辆信息")
    @PutMapping("/update")
    public Car updateCar(@RequestBody Car car) {
        return carService.update(car);
    }

    @Operation(summary = "车辆出发")
    @PutMapping("/setout")
    public Car setout(@RequestBody CarSetoutRequest carSetoutRequest) {
        return carService.setout(carSetoutRequest);
    }

    @Operation(summary = "车辆到达")
    @PutMapping
    public Car arrive(long carId) {
        return carService.arrive(carId);
    }

}
