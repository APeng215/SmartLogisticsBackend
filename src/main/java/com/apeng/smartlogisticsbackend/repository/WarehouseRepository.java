package com.apeng.smartlogisticsbackend.repository;

import com.apeng.smartlogisticsbackend.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
