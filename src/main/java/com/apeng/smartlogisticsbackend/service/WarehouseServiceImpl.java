package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.repository.CarRepository;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import com.apeng.smartlogisticsbackend.repository.ShelveRepository;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ShelveService shelveService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarRepository carRepository;


    @Override
    public Long insert(Warehouse warehouse) {
        Long ret = warehouseRepository.save(warehouse).getId();
        int rows = 2; // Number of rows
        int shelvesPerRow = (int) Math.ceil((double) warehouse.getCapacity() / rows);
        int count=0;
        for (int row = 0; row < rows; row++) {
            for (int i = 0; i < shelvesPerRow; i++) {
                int posY = i + 1;
                int posX = row + 1;
                if(count >= warehouse.getCapacity()) {
                    break;
                }
                Shelve shelve = new Shelve(posX, posY, warehouse);
                count++;
                shelveService.insert(shelve);
            }
        }
        return ret;
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
            key.setState("已入库");
            shelveService.update(value);
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
        List<Shelve> shelveList = shelveService.findShelvesByWarehouseId(warehouseId);
        Map<Order, Shelve> orderShelveMap = new HashMap<>();
        int startX = 0, startY = 0;
        for (Order order : orders) {
            Shelve bestShelve = findBestShelve(order, shelveList,startX,startY);
            if (bestShelve != null) {
                orderShelveMap.put(order, bestShelve);
                bestShelve.setLoadFactor(bestShelve.getLoadFactor()+order.getProductNum());

                startX = bestShelve.getPosX();
                startY = bestShelve.getPosY();
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order " + order.getId() + " cannot be placed on any shelve.");
            }
        }
        return orderShelveMap;
    }
    private Shelve findBestShelve(Order order, List<Shelve> shelveList, int startX, int startY) {
        Shelve bestShelve = null;
        int maxLoadDifference = Integer.MIN_VALUE;
        int minDistance = Integer.MAX_VALUE;

        for (Shelve shelve : shelveList) {
            if (shelveService.canAddOrder(shelve,order)) {
                int loadDifference = shelve.getLoadFactor();
                int distance = shelveService.distanceFrom(shelve,startX, startY);

                // 选择剩余容量最大且距离最短的货架
                if (loadDifference > maxLoadDifference || (loadDifference == maxLoadDifference && distance < minDistance)) {
                    bestShelve = shelve;
                    maxLoadDifference = loadDifference;
                    minDistance = distance;
                }
            }
        }

        return bestShelve;
    }
    @Override
    public synchronized void outbound(OutboundRequest outboundRequest) {
        orderRepository.findAllById(outboundRequest.orderIds()).forEach(order -> {
            order.setShelve(null);
            order.setCar(carRepository.findById(outboundRequest.carId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find the car!")));
            orderRepository.save(order);
        });
    }
}
