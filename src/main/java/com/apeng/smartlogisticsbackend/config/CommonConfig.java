package com.apeng.smartlogisticsbackend.config;

import com.apeng.smartlogisticsbackend.dto.CarInsertRequest;
import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OrderResponse;
import com.apeng.smartlogisticsbackend.entity.*;
import com.apeng.smartlogisticsbackend.entity.embeddable.LngLat;
import com.apeng.smartlogisticsbackend.repository.UserRepository;
import com.apeng.smartlogisticsbackend.service.CarService;
import com.apeng.smartlogisticsbackend.service.OrderService;
import com.apeng.smartlogisticsbackend.service.ProductService;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
            //仓库创建
            warehouseService.insert(new Warehouse("郑州大学松园驿站", "郑州大学松园", 12, new LngLat(113.531004,34.822673)));
            warehouseService.insert(new Warehouse("郑州大学菊园驿站","郑州大学菊园",6,new LngLat(113.53078,34.820693)));
            warehouseService.insert(new Warehouse("郑州大学荷园驿站", "郑州大学荷园洗浴中心", 10, new LngLat(113.530395,34.814632)));
            warehouseService.insert(new Warehouse("郑州大学柳园驿站", "郑州大学柳园", 8, new LngLat(113.529802,34.812241)));

            //车辆创建
            carService.insert(new CarInsertRequest("万浩", 1L));
            carService.insert(new CarInsertRequest("车车", 2L));
            carService.insert(new CarInsertRequest("神奈", 3L));
            carService.insert(new CarInsertRequest("李师傅", 4L));
        };
    }

    @org.springframework.core.annotation.Order(2)
    @Bean
    public CommandLineRunner addProducts(ProductService productService, OrderService orderService, WarehouseService warehouseService) {
        return (args) -> {

            //商品创建
            productService.insert(new Product("相机", "Soni", BigDecimal.valueOf(998)));
            productService.insert(new Product("手机", "Apple", BigDecimal.valueOf(6000)));
            productService.insert(new Product("笔记本", "Apple", BigDecimal.valueOf(8000)));
            productService.insert(new Product("铅笔", "淘宝", BigDecimal.valueOf(2)));

            //订单创建
            orderService.insert(new Order(productService.findById(1L),1,warehouseService.findById(2L)));
            orderService.insert(new Order(productService.findById(2L),2,warehouseService.findById(3L)));
            orderService.insert(new Order(productService.findById(3L),3,warehouseService.findById(4L)));
            orderService.insert(new Order(productService.findById(4L),4,warehouseService.findById(4L)));
            orderService.insert(new Order(productService.findById(1L),4,warehouseService.findById(4L)));

            //订单入库
            List<Long> list=new ArrayList<>();
            List<OrderResponse> orderResponseList=orderService.findAll();
            for(int i=0;i<orderResponseList.size();++i){
                list.add(orderResponseList.get(i).getOrder().getId());
            }
            warehouseService.inbound(new InboundRequest(list,1L));
        };
    }


}
