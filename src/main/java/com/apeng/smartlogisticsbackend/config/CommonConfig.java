package com.apeng.smartlogisticsbackend.config;

import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.entity.User;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.entity.sub.Authority;
import com.apeng.smartlogisticsbackend.repository.ProductRepository;
import com.apeng.smartlogisticsbackend.repository.UserRepository;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
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
    public CommandLineRunner addProducts(ProductRepository productRepository) {
        return (args) -> {
            productRepository.save(new Product("相机", "Soni", BigDecimal.valueOf(998)));
            productRepository.save(new Product("手机", "Apple", BigDecimal.valueOf(6000)));
        };
    }

    @Bean
    public CommandLineRunner addWarehouse(WarehouseRepository warehouseRepository) {
        return (args) -> {
            warehouseRepository.save(new Warehouse("荷园驿站", "荷园6号楼", 660));
            warehouseRepository.save(new Warehouse("柳园驿站", "柳园6号楼", 20));
            warehouseRepository.save(new Warehouse("松园驿站", "松园5号楼", 220));
        };
    }

}
