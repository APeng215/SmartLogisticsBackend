package com.apeng.smartlogisticsbackend.dto;

import com.apeng.smartlogisticsbackend.entity.Warehouse;

import java.util.List;

public record AutoInAndOutboundRequest(long carId, List<Warehouse> passWarehouses) {
}
