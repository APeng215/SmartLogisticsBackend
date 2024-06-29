package com.apeng.smartlogisticsbackend.dto;

import java.util.List;

public record InboundRequest(List<Long> orderIds, long warehouseId) {
}
