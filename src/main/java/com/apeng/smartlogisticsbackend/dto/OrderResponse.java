package com.apeng.smartlogisticsbackend.dto;

import com.apeng.smartlogisticsbackend.entity.Order;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

@Data
public class OrderResponse {

    private final Order order;
    private final int orderPrice;

    public OrderResponse(Order order) {
        this.order = order;
        this.orderPrice = order.getProductNum() * order.getProduct().getPrice().intValue();
    }

}
