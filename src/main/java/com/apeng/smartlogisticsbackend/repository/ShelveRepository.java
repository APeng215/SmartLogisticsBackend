package com.apeng.smartlogisticsbackend.repository;

import com.apeng.smartlogisticsbackend.entity.Shelve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShelveRepository extends JpaRepository<Shelve, Long> {
    List<Shelve> findShelvesByWarehouseId(Long id);
}
