package com.apeng.smartlogisticsbackend.repository;

import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByUser(User user);

}
