package com.apeng.smartlogisticsbackend.config;

import com.apeng.smartlogisticsbackend.entity.User;
import com.apeng.smartlogisticsbackend.entity.sub.Authority;
import com.apeng.smartlogisticsbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

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

}
