package com.apeng.smartlogisticsbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    private String transporter;
    private String state;
    @ManyToOne
    private Warehouse warehouse;

    public Car(String transporter, String state, Warehouse warehouse) {
        this.transporter = transporter;
        this.state = state;
        this.warehouse = warehouse;
    }

}
