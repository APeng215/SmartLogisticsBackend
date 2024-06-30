package com.apeng.smartlogisticsbackend.entity;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
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
    @ManyToOne
    private Warehouse targetWarehouse;
    @ManyToOne
    private Car car;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Shelve shelve;
    private int productNum = 1;

    public Order(Product product, int productNum, Warehouse targetWarehouse) {
        createTime = updateTime = new Date();
        state = "待入库";
        this.product = product;
        this.productNum = productNum;
        this.targetWarehouse = targetWarehouse;
    }

}
