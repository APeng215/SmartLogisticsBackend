package com.apeng.smartlogisticsbackend.repository;


import com.apeng.smartlogisticsbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, String> {
}