package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.CarInsertRequest;
import com.apeng.smartlogisticsbackend.dto.CarSetoutRequest;
import com.apeng.smartlogisticsbackend.entity.Car;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    OrderService orderService;

    @Autowired
    ShelveService shelveService;
    
    @Override
    public Car insert(CarInsertRequest carInsertRequest) {
        Warehouse warehouse = warehouseService.findById(carInsertRequest.warehouseId());
        validate(carInsertRequest, warehouse);
        return carRepository.save(createCar(carInsertRequest, warehouse));
    }

    private static Car createCar(CarInsertRequest carInsertRequest, Warehouse warehouse) {
        return new Car(carInsertRequest.transporter(), warehouse);
    }

    private static void validate(CarInsertRequest carInsertRequest, Warehouse warehouse) {
        validateWarehouse(warehouse);
        validateTransporter(carInsertRequest);
    }

    private static void validateTransporter(CarInsertRequest carInsertRequest) {
        if (carInsertRequest.transporter() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transporter name is empty!");
        }
    }

    private static void validateWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Warehouse is null!");
        }
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        carRepository.deleteAllById(idList);
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Car findByTransporter(String name) {
        return carRepository.findByTransporter(name);
    }

    @Override
    public synchronized Car setout(CarSetoutRequest carSetoutRequest) {
        Car car = carRepository.findById(carSetoutRequest.carId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Car %d not found!", carSetoutRequest.carId())));
        Warehouse targetWarehouse = warehouseService.findById(carSetoutRequest.targetWarehouseId());
        validaCarState(car);
        doSetout(car, targetWarehouse);
        return carRepository.save(car);
    }

    private static void validaCarState(Car car) {
        if (!car.getState().equals("停靠中")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is not in parking!");
        }
    }

    private void doSetout(Car car, Warehouse targetWarehouse) {
        car.setTargetWarehouse(targetWarehouse);
        List<Order> orders = new ArrayList<>();
        shelveService.findShelvesByWarehouseId(car.getWarehouse().getId()).forEach(shelve -> {
            orderService.findOrdersByShelveId(shelve.getId()).forEach(orderResponse -> {
                orders.add(orderResponse.getOrder());
            });
        });
        car.setState("运输中");
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car update(Car car) {
        if (car.getId() == null) return null;
        return carRepository.save(car);
    }

    @Override
    public Car arrive(long carId) {
        final Car car = readCar(carId);
        validateState(car);
        doCarArrival(car);
        dischargeCargo(carId, car);
        return carRepository.save(car);
    }

    private void dischargeCargo(long carId, Car car) {
        orderService.findOrdersByCarId(carId).forEach(order -> {
            if (order.getTargetWarehouse().getId().equals(car.getWarehouse().getId()) && order.getState().equals("已出库")) {
                order.setUpdateTime(new Date());
                order.setState("已送达");
                order.setCar(null);
            }
        });
    }

    private static void doCarArrival(Car car) {
        car.setState("停靠中");
        car.setWarehouse(car.getTargetWarehouse());
        car.setTargetWarehouse(null);
    }

    private Car readCar(long carId) {
        Car car = carRepository.findById(carId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car not found!")
        );
        return car;
    }

    private static void validateState(Car car) {
        if (!car.getState().equals("运输中") || car.getTargetWarehouse() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is in unsupported state!");
        }
    }
}
