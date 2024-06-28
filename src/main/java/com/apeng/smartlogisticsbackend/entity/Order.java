package com.apeng.smartlogisticsbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private Date createTime;
    private Date updateTime;
    private String state;

    @OneToMany
    private List<Product> products = new ArrayList<>();

}
