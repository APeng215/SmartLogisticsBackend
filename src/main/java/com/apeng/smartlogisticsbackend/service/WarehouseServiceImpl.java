package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import com.apeng.smartlogisticsbackend.repository.ShelveRepository;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ShelveRepository shelveRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Long insert(Warehouse warehouse) {
        return warehouseRepository.save(warehouse).getId();
    }

    @Override
    public void deleteById(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public Warehouse findById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public List<Warehouse> findByName(String name) {
        return warehouseRepository.findByName(name);
    }

    @Override
    public List<Warehouse> findByPosition(String position) {
        return warehouseRepository.findByPosition(position);
    }

    @Override
    @Transactional
    public Warehouse update(Warehouse warehouse) {
        if (warehouse.getId() == null) return null;
        return warehouseRepository.save(warehouse);
    }

    @Override
    public synchronized void inbound(InboundRequest inboundRequest) {
        List<Order> orders = orderRepository.findAllById(inboundRequest.orderIds());
        shelveSelect(orders, inboundRequest.warehouseId()).forEach((key, value) -> {
            key.setShelve(value);
            orderRepository.save(key);
        });
    }

    /**
     * 货架选择算法
     * @param orders 需要计算目标货架的订单
     * @param warehouseId 目标仓库
     * @return 订单到对应货架的映射
     */
    private Map<Order, Shelve> shelveSelect(List<Order> orders, long warehouseId) {
        return null; // TODO
    }

    @Override
    public void outbound(OutboundRequest outboundRequest) {

    }
}
