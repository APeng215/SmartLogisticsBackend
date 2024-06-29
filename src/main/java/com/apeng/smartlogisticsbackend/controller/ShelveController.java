package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.service.ShelveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShelveController {
    @Autowired
    private ShelveService shelveService;

    @GetMapping("/shelve/search/id/{id}")
    Shelve getShelveById(@PathVariable long id) {
        return shelveService.findById(id);
    }

    @GetMapping("/shelve/search")
    List<Shelve> getAllShelve()
    {
        return shelveService.findAll();
    }

    @PostMapping("/shelve/insert")
    Long insertShelve(@RequestBody Shelve shelve)
    {
        return shelveService.insert(shelve);
    }

    @DeleteMapping("/shelve/delete/id/{id}")
    void deleteShelveById(@PathVariable long id)
    {
        shelveService.deleteById(id);
    }

    @PutMapping("/shelve/update")
    Shelve updateShelve(@RequestBody Shelve shelve)
    {
        return shelveService.update(shelve);
    }
}
