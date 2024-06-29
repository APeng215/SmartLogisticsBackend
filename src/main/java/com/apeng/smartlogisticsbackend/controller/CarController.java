package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @Operation(summary = "通过ID获取车辆信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到车辆",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "404", description = "车辆未找到",
                    content = @Content)
    })
    @GetMapping("/search/id/{id}")
    public Car getCarById(@Parameter(description = "车辆的ID") @PathVariable long id) {
        return carService.findById(id);
    }

    @Operation(summary = "通过运输者姓名获取车辆列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到车辆",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "404", description = "车辆未找到",
                    content = @Content)
    })
    @GetMapping("/search/name/{name}")
    public List<Car> getCarsByTransporter(@Parameter(description = "运输者的姓名") @PathVariable String name) {
        return carService.findByTransporter(name);
    }

    @Operation(summary = "获取所有车辆信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到车辆",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))})
    })
    @GetMapping("/search")
    public List<Car> getAllCar() {
        return carService.findAll();
    }

    @Operation(summary = "添加新车辆")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "车辆添加成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))})
    })
    @PostMapping("/insert")
    public Long insertCar(@Parameter(description = "车辆信息") @RequestBody Car car) {
        return carService.insert(car);
    }

    @Operation(summary = "通过ID删除车辆")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "车辆删除成功"),
            @ApiResponse(responseCode = "404", description = "车辆未找到",
                    content = @Content)
    })
    @DeleteMapping("/delete/id/{id}")
    public void deleteCarById(@Parameter(description = "车辆的ID") @PathVariable long id) {
        carService.deleteById(id);
    }

    @Operation(summary = "更新车辆信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "车辆更新成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))})
    })
    @PutMapping("/update")
    public Car updateCar(@Parameter(description = "车辆信息") @RequestBody Car car) {
        return carService.update(car);
    }
}
