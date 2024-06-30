package com.apeng.smartlogisticsbackend.entity;

import com.apeng.smartlogisticsbackend.service.ShelveService;
import com.apeng.smartlogisticsbackend.service.ShelveServiceImpl;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String position;
    private int capacity;

    public Warehouse(String name, String position, int capacity) {
        this.name = name;
        this.position = position;
        this.capacity = capacity;
    }


}
