package com.example.reuse_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/getDataTable")
public class DataTableController {

    @GetMapping
    public List<Map<String, Integer>> getDataTable() {
        List<Map<String, Integer>> dataList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 30; i++) {
            Map<String, Integer> data = new HashMap<>();
            for (int j = 1; j < 6; j++) {  // 센서의 개수만큼 반복
                String sensorName = "sensor" + j;  // 센서의 이름 생성
                data.put(sensorName, random.nextInt(100));
            }

            dataList.add(data);
        }

        return dataList;
    }
}