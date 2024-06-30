package com.apeng.smartlogisticsbackend.dto;

public record OrderRequest(long productId, int productNum, long targetWarehouseId) {
}
