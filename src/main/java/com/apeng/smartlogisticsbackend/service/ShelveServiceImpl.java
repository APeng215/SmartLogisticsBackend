package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.repository.ShelveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelveServiceImpl implements ShelveService {

    @Autowired
    ShelveRepository repository;

    @Override
    public Long insert(Shelve shelve) {
        return repository.save(shelve).getId();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Shelve findById(Long id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Shelve> findAll() {
        return repository.findAll();
    }

    @Override
    public Shelve update(Shelve shelve) {
        if (shelve.getId() == null) return null;
        return repository.save(shelve);
    }
}
