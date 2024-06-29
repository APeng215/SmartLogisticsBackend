package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.repository.ShelveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelveServiceImpl implements ShelveService {

    @Autowired
    ShelveRepository shelveRepository;

    @Override
    public Long insert(Shelve shelve) {
        return shelveRepository.save(shelve).getId();
    }

    @Override
    public void deleteById(Long id) {
        shelveRepository.deleteById(id);
    }

    @Override
    public Shelve findById(Long id) {
        return shelveRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Shelve> findAll() {
        return shelveRepository.findAll();
    }

    @Override
    public List<Shelve> findShelvesByWarehouseId(Long id) {
        return shelveRepository.findShelvesByWarehouseId(id);
    }

    @Override
    public Shelve update(Shelve shelve) {
        if (shelve.getId() == null) return null;
        return shelveRepository.save(shelve);
    }

    @Override
    public boolean canAddOrder(Shelve shelve, Order order) {
        if(shelve.getCapacity()>=shelve.getLoadFactor()+order.getProductNum()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int distanceFrom(Shelve shelve, int startX, int startY) {
        return Math.abs(shelve.getPosX() - startX) + Math.abs(shelve.getPosY() - startY);
    }

}
