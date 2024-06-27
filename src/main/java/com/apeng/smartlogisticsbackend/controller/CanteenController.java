package com.apeng.smartlogisticsbackend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.apeng.smartlogisticsbackend.entity.Canteen;
import com.apeng.smartlogisticsbackend.repository.CanteenRepository;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/canteen")
public class CanteenController {

    private final CanteenRepository canteenRepository;

    private static final Map<Range<Double>, String> saturation2StateMapping = new HashMap<>();

    static {
        saturation2StateMapping.put(Range.from(Range.Bound.inclusive(0.0)).to(Range.Bound.exclusive(0.33)), "畅通");
        saturation2StateMapping.put(Range.from(Range.Bound.inclusive(0.33)).to(Range.Bound.exclusive(0.66)), "拥挤");
        saturation2StateMapping.put(Range.from(Range.Bound.inclusive(0.66)).to(Range.Bound.inclusive(Double.MAX_VALUE)), "爆满");
    }

    public CanteenController(CanteenRepository canteenRepository) {
        this.canteenRepository = canteenRepository;
    }

    @GetMapping("/all")
    private String getAll() {
        JSONArray canteenArray = new JSONArray();
        fillCanteenArray(canteenArray);
        return canteenArray.toJSONString();
    }

    @GetMapping("/{canteenName}")
    private String getInfo(@PathVariable String canteenName) {
        Canteen canteen = retriveCanteen(canteenName);
        JSONObject canteenObject = computeJSONObject(canteen);
        return canteenObject.toJSONString();
    }

    @PatchMapping("/{canteenName}")
    private String updatePeopleNum(@PathVariable String canteenName, @RequestParam int peopleNum) {
        Canteen canteen = retriveCanteen(canteenName);
        canteen.setPeopleNum(peopleNum);
        Canteen newCanteen = canteenRepository.save(canteen);
        return String.format("Update people number to %d successfully!: %s", peopleNum, JSON.toJSONString(newCanteen));
    }

    @PatchMapping("/{canteenName}/increasePeopleNum")
    private synchronized String increasePeopleNum(@PathVariable String canteenName) {
        Canteen canteen = retriveCanteen(canteenName);
        canteen.increasePeopleNum();
        return saveAndReturnJson(canteen);
    }

    @PatchMapping("/{canteenName}/decreasePeopleNum")
    private synchronized String decreasePeopleNum(@PathVariable String canteenName) {
        Canteen canteen = retriveCanteen(canteenName);
        checkEmpty(canteen);
        canteen.decreasePeopleNum();
        return saveAndReturnJson(canteen);
    }

    @PutMapping
    private String updateCanteen(@RequestBody String requestedCanteen) {
        Canteen canteen = JSON.parseObject(requestedCanteen, Canteen.class);
        canteenRepository.save(canteen);
        return String.format("Update successfully: %s", requestedCanteen);
    }

    private static void checkEmpty(Canteen canteen) {
        if (canteen.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Invalid request: Cannot decrease number, it is already zero. Requested canteen:\n%s",
                            JSON.toJSONString(canteen)
                    )
            );
        }
    }

    private void fillCanteenArray(JSONArray canteenArray) {
        canteenRepository.findAll().forEach(canteen -> {
            JSONObject canteenObject = computeJSONObject(canteen);
            canteenArray.add(canteenObject);
        });
    }

    private String saveAndReturnJson(Canteen canteen) {
        return JSON.toJSONString(canteenRepository.save(canteen));
    }

    private static JSONObject computeJSONObject(Canteen canteen) {
        double saturation = computeSaturation(canteen);
        JSONObject canteenObject = JSONObject.from(canteen);
        putSaturation(saturation, canteenObject);
        putState(saturation, canteenObject);
        return canteenObject;
    }

    private static void putSaturation(double saturation, JSONObject canteenObject) {
        canteenObject.put("saturation", saturation);
    }

    private static void putState(double saturation, JSONObject canteenObject) {
        for (Map.Entry<Range<Double>, String> entry : saturation2StateMapping.entrySet()) {
            if (entry.getKey().contains(saturation)) {
                canteenObject.put("state", entry.getValue());
                return;
            }
        }
    }

    private static double computeSaturation(Canteen canteen) {
        return (double) canteen.getPeopleNum() / canteen.getCapacity();
    }

    private Canteen retriveCanteen(String canteenName) {
        return canteenRepository.findById(canteenName).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("%s canteen not found", canteenName)));
    }

}
