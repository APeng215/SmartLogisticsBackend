package com.apeng.smartlogisticsbackend.config;

import com.apeng.smartlogisticsbackend.dto.CarInsertRequest;
import com.apeng.smartlogisticsbackend.entity.*;
import com.apeng.smartlogisticsbackend.repository.UserRepository;
import com.apeng.smartlogisticsbackend.service.CarService;
import com.apeng.smartlogisticsbackend.service.OrderService;
import com.apeng.smartlogisticsbackend.service.ProductService;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Configuration
public class CommonConfig {

    public static String ADMIN_USERNAME = "admin";
    public static String ADMIN_PASSWORD = "123456";

    /**
     * 添加管理员用户
     *
     * @param repository
     * @return
     */
    @Bean
    public CommandLineRunner addAdminUser(UserRepository repository) {
        return (args) -> {
            repository.save(new User(CommonConfig.ADMIN_USERNAME, CommonConfig.ADMIN_PASSWORD));
        };
    }

    @org.springframework.core.annotation.Order(1)
    @Bean
    public CommandLineRunner addWarehouseAndCar(WarehouseService warehouseService, CarService carService) {
        return (args) -> {
            warehouseService.insert(new Warehouse("荷园驿站", "荷园6号楼", 4));
            warehouseService.insert(new Warehouse("柳园驿站", "柳园6号楼", 5));
            warehouseService.insert(new Warehouse("松园驿站", "松园5号楼", 6));
            carService.insert(new CarInsertRequest("万浩", 1L));
            carService.insert(new CarInsertRequest("车车", 1L));
            carService.insert(new CarInsertRequest("神奈", 1L));
        };
    }

    @org.springframework.core.annotation.Order(2)
    @Bean
    public CommandLineRunner addProducts(ProductService productService, OrderService orderService, WarehouseService warehouseService) {
        return (args) -> {
            productService.insert(new Product("相机", "Soni", BigDecimal.valueOf(998)));
            productService.insert(new Product("手机", "Apple", BigDecimal.valueOf(6000)));
            orderService.insert(new Order(productService.findById(1L),2,warehouseService.findById(1L)));
            orderService.insert(new Order(productService.findById(1L),3,warehouseService.findById(2L)));
            orderService.insert(new Order(productService.findById(1L),4,warehouseService.findById(2L)));
        };
    }

}
