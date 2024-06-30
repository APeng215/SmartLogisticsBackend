package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.repository.CarRepository;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import com.apeng.smartlogisticsbackend.repository.ShelveRepository;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.apache.tomcat.websocket.server.WsHttpUpgradeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
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
            key.setUpdateTime(new Date());
            shelveService.update(value);
            orderRepository.save(key);
        });
        if(inboundRequest.carId()!=0)
        {
            //入库的订单与相关联的车辆解绑
            orderRepository.findOrdersByCarId(inboundRequest.carId()).forEach((key)->{
                key.setCar(null);
                orderRepository.save(key);
            });
        }
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
            Car car = carRepository.findById(outboundRequest.carId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Car %d not founded!", outboundRequest.carId())));
            validate(outboundRequest, order, car);
            doOutbound(outboundRequest, order);
        });
    }

    private static void validate(OutboundRequest outboundRequest, Order order, Car car) {
        validateInbounding(order);
        validateCarPos(outboundRequest, order, car);
        validateCarState(car);
    }

    private static void validateCarState(Car car) {
        if (!car.getState().equals("停靠中")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Car %d is not in transit!", car.getId()));
        }
    }

    private void doOutbound(OutboundRequest outboundRequest, Order order) {
        decreaseShelveLoadFactor(order);
        boundOrder(outboundRequest, order);
    }

    private static void validateCarPos(OutboundRequest outboundRequest, Order order, Car car) {
        if (!isCarInWarehouseOfOrder(order, car)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Car %d is not in Warehouse %d!", outboundRequest.carId(), order.getShelve().getWarehouse().getId()));
        }
    }

    private static boolean isCarInWarehouseOfOrder(Order order, Car car) {
        return order.getShelve().getWarehouse().equals(car.getWarehouse());
    }

    private static void validateInbounding(Order order) {
        if (order.getShelve() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Order(Id %d) has not been inbounded!", order.getId()));
        }
    }

    private void boundOrder(OutboundRequest outboundRequest, Order order) {
        order.setShelve(null);
        order.setCar(carRepository.findById(outboundRequest.carId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find the car!")));
        order.setState("已出库");
        order.setUpdateTime(new Date());
        orderRepository.save(order);
    }

    private void decreaseShelveLoadFactor(Order order) {
        Shelve shelve = order.getShelve();
        shelve.setLoadFactor(shelve.getLoadFactor() - order.getProductNum());
        shelveService.update(shelve);
    }
}
