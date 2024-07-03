package com.apeng.smartlogisticsbackend.entity;

import com.apeng.smartlogisticsbackend.entity.embeddable.LngLat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LngLat lngLat;

    public Warehouse(String name, String position, int capacity) {
        this.name = name;
        this.position = position;
        this.capacity = capacity;
    }

    public Warehouse(String name, String position, int capacity, LngLat lngLat) {
        this.name = name;
        this.position = position;
        this.capacity = capacity;
        this.lngLat = lngLat;
    }


}
