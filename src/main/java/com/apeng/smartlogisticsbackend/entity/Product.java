package com.apeng.smartlogisticsbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String mallName;
    private BigDecimal price;

    public Product(String name, String mallName, BigDecimal price) {
        this.name = name;
        this.mallName = mallName;
        this.price = price;
    }

}
