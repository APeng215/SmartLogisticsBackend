package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.entity.Warehouse;

import java.util.List;

public interface WarehouseService {
    Long insert(Warehouse warehouse);
    void deleteById(Long id);
    void deleteByIdList(List<Long> idList);
    Warehouse findById(Long id);
    List<Warehouse> findAll();
    List<Warehouse> findByName(String name);
    List<Warehouse> findByPosition(String position);
    Warehouse update(Warehouse warehouse);
    boolean inbound(InboundRequest inboundRequest);
    List<Long> outbound(OutboundRequest outboundRequest);
}
