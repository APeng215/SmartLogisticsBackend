package com.apeng.smartlogisticsbackend.repository;

import com.apeng.smartlogisticsbackend.entity.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DishRepository extends CrudRepository<Dish, Long> {
}
