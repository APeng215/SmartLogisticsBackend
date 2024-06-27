package com.apeng.smartlogisticsbackend.entity;

import com.apeng.smartlogisticsbackend.entity.sub.Nutrition;
import com.apeng.smartlogisticsbackend.entity.sub.Weighting;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Dish {


    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private double price;
    private String location;
    @Column(length = 65555)
    private String image_url;
    private String category;

    @Embedded
    private Nutrition nutrition = new Nutrition();

    @Embedded
    private Weighting weighting = new Weighting();


}
