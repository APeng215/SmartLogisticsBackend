package com.apeng.smartlogisticsbackend.dto;

import java.util.List;

public record OutboundRequest(List<Long> orderIds, Long carId) {
}
