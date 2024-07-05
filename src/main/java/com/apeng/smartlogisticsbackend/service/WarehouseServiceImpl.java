package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.AutoInAndOutboundRequest;
import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OrderResponse;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.repository.CarRepository;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ShelveService shelveService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

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
    public void deleteByIdList(List<Long> idList) {
        warehouseRepository.deleteAllById(idList);
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
    public synchronized boolean inbound(InboundRequest inboundRequest) {
        List<Order> orders = orderRepository.findAllById(inboundRequest.orderIds());
        shelveSelect(orders, inboundRequest.warehouseId()).forEach((key, value) -> {
            key.setShelve(value);
            key.setUpdateTime(new Date());
            key.setState("已入库");
            shelveService.update(value);
            Car car= key.getCar();
            if(car!=null){
                car.setCapacity(car.getCapacity()+key.getProductNum());
                if(car.getWarehouse().equals(key.getTargetWarehouse())){
                    key.setState("已送达");
                    car.setTargetWarehouse(null);
                }
                carRepository.save(car);
            }
            key.setCar(null);
            orderRepository.save(key);
        });
        return true;
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
            } else {
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
    public synchronized List<Long> outbound(OutboundRequest outboundRequest) {
        Car car = carRepository.findById(outboundRequest.carId()).orElseThrow();
        List<Order> orders2Outbound = new LinkedList<>();
        fillOrders2Outbound(outboundRequest, car, orders2Outbound);
        // 必须先计算路径再进行出库动作！
        List<Long> path = calculateShortestPaths(orders2Outbound);
        doOutbounds(outboundRequest, orders2Outbound);
        return path;
    }

    /**
     * 途径仓库的时候，出库选择订单只选择那些目标仓库为下一站仓库的订单
     * 在起始的仓库出发时，获取能最大容量的订单
     */
    @Override
    public boolean autoInAndOutbound(AutoInAndOutboundRequest autoInAndOutboundRequest) {
        long carId = autoInAndOutboundRequest.carId();
        List<Warehouse> passWarehouses = autoInAndOutboundRequest.passWarehouses();
        Car car = carRepository.findById(carId).orElseThrow(RuntimeException::new);
        car.setState("停靠中");
        Warehouse currentWarehouse = car.getWarehouse();
        Warehouse targetWarehouse = car.getTargetWarehouse();
        if(targetWarehouse==null){
            return false;
        }
        for(int k=0;k<=passWarehouses.size();++k){
            Warehouse nextWarehouse;
            if(k==passWarehouses.size()){
                nextWarehouse=targetWarehouse;
            }else{
                nextWarehouse = passWarehouses.get(k);
            }

            //currentWarehouse出货
            List<Long> orderIdList =new ArrayList<>();
            int totalProductNum=0;
            List<Warehouse> filterWarehouseList = new ArrayList<>(passWarehouses.subList(k,passWarehouses.size()));
            filterWarehouseList.add(targetWarehouse);
            List<OrderResponse> orderResponseList = selectOrdersByWarehouseList(orderService.findOrdersByWarehouseId(currentWarehouse.getId()),filterWarehouseList);


            for(int i=0;i<orderResponseList.size();++i){
                OrderResponse orderResponse = orderResponseList.get(i);
                orderIdList.add(orderResponse.getOrder().getId());
                totalProductNum+=orderResponse.getOrder().getProductNum();
            }
            if(car.getCapacity()-totalProductNum>=0){
                outbound(new OutboundRequest(orderIdList,carId));
            }else{
                outbound(new OutboundRequest(orderIdList.subList(0, car.getCapacity()),carId));
            }
            //nextWarehouse入货
            orderIdList.clear();
            List<Order> orderList = orderService.findOrdersByCarId(carId);
            for(int j=0;j<orderList.size();++j)
            {
                Order order = orderList.get(j);
                if(order.getTargetWarehouse().equals(nextWarehouse)){
                    orderIdList.add(order.getId());
                }
            }
            car.setWarehouse(nextWarehouse);
            inbound(new InboundRequest(orderIdList,nextWarehouse.getId()));
            currentWarehouse=nextWarehouse;
        }
        return true;
    }
    //对 orderResponseList 进行过滤，以便保留其中 targetWarehouse 在 warehouses 列表中存在的 OrderResponse。
    private List<OrderResponse> selectOrdersByWarehouseList(List<OrderResponse> orderResponseList, List<Warehouse> warehouses){
        return orderResponseList.stream()
                .filter(orderResponse -> warehouses.contains(orderResponse.getOrder().getTargetWarehouse()))
                .collect(Collectors.toList());
    }

    private List<Long> calculateShortestPaths(final List<Order> orders2Outbound) {
        List<Long> path = new ArrayList<>();
        int startX = 0, startY = 0;

        // Create a list of shelves to visit
        List<Shelve> shelvesToVisit = new ArrayList<>();
        for (Order order : orders2Outbound) {
            shelvesToVisit.add(order.getShelve());
        }

        while (!shelvesToVisit.isEmpty()) {
            // Find the nearest shelve
            Shelve nearestShelve = null;
            double nearestDistance = Double.MAX_VALUE;
            for (Shelve shelve : shelvesToVisit) {
                double distance = calculateDistance(startX, startY, shelve.getPosX(), shelve.getPosY());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestShelve = shelve;
                }
            }

            // Add the nearest shelve ID to the path
            if (nearestShelve != null) {
                path.add(nearestShelve.getId());
                startX = nearestShelve.getPosX();
                startY = nearestShelve.getPosY();
                shelvesToVisit.remove(nearestShelve);
            }
        }
        return path;
    }

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void doOutbounds(OutboundRequest outboundRequest, List<Order> orders2Outbound) {
        orders2Outbound.forEach(order -> doOutbound(outboundRequest, order));
    }

    private void fillOrders2Outbound(OutboundRequest outboundRequest, Car car, List<Order> orders2Outbound) {
        orderRepository.findAllById(outboundRequest.orderIds()).forEach(order -> {
            if (validate(order, car)) {
                orders2Outbound.add(order);
            }
        });
    }

    private boolean validate(Order order, Car car) {
        return validateInbounding(order) && validateCarPos(order, car) && validateCarState(car);
    }

    private static boolean validateCarState(Car car) {
        return car.getState().equals("停靠中");
    }

    private void doOutbound(OutboundRequest outboundRequest, Order order) {
        decreaseShelveLoadFactor(order);
        boundOrder(outboundRequest, order);
    }

    private static boolean validateCarPos(Order order, Car car) {
        return isCarInWarehouseOfOrder(order, car);
    }

    private static boolean isCarInWarehouseOfOrder(Order order, Car car) {
        return order.getShelve().getWarehouse().equals(car.getWarehouse());
    }

    private static boolean validateInbounding(Order order) {
        return order.getShelve() != null && order.getState().equals("已入库");
    }

    private void boundOrder(OutboundRequest outboundRequest, Order order) {
        order.setShelve(null);
        Car car = carRepository.findById(outboundRequest.carId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find the car!"));
        car.setCapacity(car.getCapacity()-order.getProductNum());
        carRepository.save(car);
        order.setCar(car);
        order.setState("已出库");
        order.setUpdateTime(new Date());
        orderRepository.save(order);
    }


    private void decreaseShelveLoadFactor(Order order) {
        Shelve shelve = order.getShelve();
        shelve.setLoadFactor(shelve.getLoadFactor() - order.getProductNum());
        shelveService.update(shelve);
    }

    @ToString
    private static class PickupPaths {

        Map<Order, List<Shelve>> pathMap = new HashMap<>();

        private PickupPaths addPath(Order order, Shelve nextPath) {
            pathMap.merge(order, List.of(nextPath), (paths, value) -> {
                paths.addAll(value);
                return paths;
            });
            return this;
        }

        private PickupPaths addPaths(Order order, List<Shelve> nextPaths) {
            pathMap.merge(order, nextPaths, (paths, value) -> {
                paths.addAll(value);
                return paths;
            });
            return this;
        }

        private String getPath4IdDescription() {
            Map<Long, List<Long>> pathMap4Id = new HashMap<>();
            pathMap.forEach(((order, shelves) -> {
                pathMap4Id.put(order.getId(), shelves.stream().map(Shelve::getId).collect(Collectors.toList()));
            }));
            return pathMap4Id.toString();
        }

    }

}
