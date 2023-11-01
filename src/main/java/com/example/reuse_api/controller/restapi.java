package com.example.reuse_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 가정, 위성에서 보낸 데이터 $(시작) %(종료) 기호로 구분하기ㅁㅇㅁㅇ
// Map은 key와 value의 쌍으로 이루어진 데이터의 집합이다.
// Key 값을 String, value값을 Object형으로 put 메소드를 통해 입력가능
@RestController
public class restapi {
    @PostMapping("/sensor")
    public String sensorData(@RequestBody SatelliteData data) throws IOException {
        String satelliteId = data.getName();
        String streamData = data.getData();

        System.out.println(satelliteId);
        System.out.println(streamData);

        String[] dataPart = streamData.split("#");

        Map<String, String> sensorDataInfo = new HashMap<>();
        // 맨처음 #으로 시작되는 부분은 공백처리이므로 맨처음껀 없는데이터임
        // 그러므로 맨처음부분을 넘기기위해 boolean 처리함

        String sensorName = null;

        boolean firstPart = true;

        for (String part : dataPart) {
            if (firstPart) {
                // 맨 처음 부분은 공백 처리
                firstPart = false;
            } else if (sensorName == null) {
                // 센서 이름을 설정
                sensorName = part;
            } else {
                // 센서 값을 설정
                String sensorValue = part;

                //데이터 가공
                String processedDataValue = DataProcessing.processData(sensorName ,sensorValue);

                sensorDataInfo.put(sensorName, processedDataValue);

                // 다음 센서 이름을 기다림
                sensorName = null;
            }

        }
        System.out.println(sensorDataInfo);


        return sensorDataInfo.toString();
    }


}
