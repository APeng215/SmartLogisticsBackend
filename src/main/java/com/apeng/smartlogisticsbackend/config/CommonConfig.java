package com.apeng.smartlogisticsbackend.config;

import com.apeng.smartlogisticsbackend.entity.*;
import com.apeng.smartlogisticsbackend.entity.sub.Authority;
import com.apeng.smartlogisticsbackend.repository.CarRepository;
import com.apeng.smartlogisticsbackend.repository.UserRepository;
import com.apeng.smartlogisticsbackend.service.CarService;
import com.apeng.smartlogisticsbackend.service.OrderService;
import com.apeng.smartlogisticsbackend.service.ProductService;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import com.apeng.smartlogisticsbackend.service.OrderService;
import com.apeng.smartlogisticsbackend.service.ProductService;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import com.apeng.smartlogisticsbackend.service.OrderService;
import com.apeng.smartlogisticsbackend.service.ProductService;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class CommonConfig {

    Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    public static String ADMIN_USERNAME = "admin";
    public static String ADMIN_PASSWORD = "123456";

    @Bean
    public RestTemplate configureRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 配置 RestTemplateBuilder 以使用管理员认证
     *
     * @param restTemplateBuilder
     * @return
     */
    @Bean
    RestOperations configureRestOperations(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.basicAuthentication(ADMIN_USERNAME, ADMIN_PASSWORD).build();
    }

    /**
     * 添加管理员用户
     *
     * @param repository
     * @return
     */
    @Bean
    public CommandLineRunner addAdminUser(UserRepository repository) {
        return (args) -> {
            repository.save(new User(CommonConfig.ADMIN_USERNAME, CommonConfig.ADMIN_PASSWORD, Set.of(new Authority(CommonConfig.ADMIN_USERNAME, "ROLE_ADMIN"), new Authority(CommonConfig.ADMIN_USERNAME, "ROLE_USER"))));
            repository.save(new User("player", "123456"));
        };
    }

    @Bean
    public CommandLineRunner addProducts(ProductService productService,OrderService orderService) {
        return (args) -> {
            productService.insert(new Product("相机", "Soni", BigDecimal.valueOf(998)));
            productService.insert(new Product("手机", "Apple", BigDecimal.valueOf(6000)));
            orderService.insert(new Order(productService.findById(1L),2,null));
            orderService.insert(new Order(productService.findById(1L),3,null));
            orderService.insert(new Order(productService.findById(1L),4,null));
        };
    }

    @Bean
    public CommandLineRunner addWarehouseAndCar(WarehouseService warehouseService, CarService carService) {
        return (args) -> {
            warehouseService.insert(new Warehouse("荷园驿站", "荷园6号楼", 4));
            warehouseService.insert(new Warehouse("柳园驿站", "柳园6号楼", 5));
            warehouseService.insert(new Warehouse("松园驿站", "松园5号楼", 6));
            carService.insert(new Car("万浩", "停靠中", warehouseService.findById(1L)));
            carService.insert(new Car("车车", "停靠中", warehouseService.findById(1L)));
            carService.insert(new Car("神奈", "停靠中", warehouseService.findById(1L)));
        };
    }

}
