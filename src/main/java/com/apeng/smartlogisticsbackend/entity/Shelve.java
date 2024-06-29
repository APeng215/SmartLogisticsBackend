package com.apeng.smartlogisticsbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shelve {

    @Id
    @GeneratedValue
    private Long id;

    private int posX, posY;
    private int capacity;
    private int loadFactor;
    @ManyToOne
    private Warehouse warehouse;

}
