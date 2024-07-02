package com.apeng.smartlogisticsbackend.dto;

import com.apeng.smartlogisticsbackend.entity.Warehouse;

public record CarInsertRequest(String transporter, Long warehouseId) {
}
