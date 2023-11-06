package com.example.reuse_api.controller;

import com.example.reuse_api.entity.AllStoreData;
import com.example.reuse_api.entity.ImageData;
import com.example.reuse_api.entity.getsetdata;
import com.example.reuse_api.entity.SatelliteData;
import com.example.reuse_api.service.AllService;
import com.example.reuse_api.service.DBService;
import com.example.reuse_api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 가정, 위성에서 보낸 데이터 $(시작) %(종료) 기호로 구분하기
// Map은 key와 value의 쌍으로 이루어진 데이터의 집합이다.
// Key 값을 String, value값을 Object형으로 put 메소드를 통해 입력가능
@RestController
public class restapi {

    private StringBuilder buffer = new StringBuilder(); //얘는 buffer을 사용
    private SerialDataParser parser = new SerialDataParser();

    @Autowired
    private DBService dbService;

    @Autowired
    private AllService allService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/sensor")
    public String sensorData(@RequestBody getsetdata data) throws IOException {
        String satelliteId = data.getName();
        String streamData = data.getData();

        processMessage(streamData);

        return "Sensor data processed successfully";
    }

//    private static final String Sensor_name_parttern = "(.*)\\.(.*)"; // sensor1.Anolog 그룹 1, 2를 .으로 구분
//    private static final Pattern namepattern = Pattern.compile(Sensor_name_parttern);

    private void processMessage(String message) throws IOException {
        Map<String, String> sensorData = parser.parseSerialData(message);

        for (String sensorName : sensorData.keySet()) {
            System.out.println("Sensor Name: " + sensorName + ", Sensor Value: " + sensorData.get(sensorName));
            AllStoreData allStoreData = new AllStoreData();
            ImageData imageData = new ImageData();

            //SateliteData는 데이터베이스를 새로 구조짤때 사용해야할듯 - 로그인 기능으로 사용자의 고유 id값이 생긴경우 사용

//            Matcher matcher = namepattern.matcher(sensorName);
//            String Name = matcher.group(1);
//            String Datatype = matcher.group(2);
            String[] parts = sensorName.split("\\.");

            if (parts.length == 2) {
                String dataType = parts[1]; // 두 번째 요소가 데이터 타입
                String Name = parts[0];
                // dataType을 사용하여 처리
                if (dataType.equals("Image")) {
                    imageData.setName(Name);
                    byte[] imageBytes = Base64.getDecoder().decode(sensorData.get(sensorName));
                    imageData.setData(imageBytes);
                    imageService.saveimageDB(imageData);
                } else { // 그외 나머지 센서들은 .DataType이 붙음
                    allStoreData.setName(sensorName);
                    allStoreData.setData(sensorData.get(sensorName));
                    allService.saveAllDB(allStoreData);
                }
            }
        }
    }
        public class SerialDataParser {
            // 센서 데이터를 분석하기위한 정규 표현식, #으로 시작, =로 구분되는 두개의 문자열을 찾아낸다.
            // 다음 #이나 $이 올때까지 하나의 센서데이터로 본다.
            private static final String SENSOR_DATA_PATTERN = "#(.*?)=(.*?)(?=#|$)";

            // Pattern.compile 메서드를 이용하여 정규표현식을 컴파일한다. (Pattern은 문자열 데이터를 분석에 사용)
            private static final Pattern pattern = Pattern.compile(SENSOR_DATA_PATTERN);

            public Map<String, String> parseSerialData(String data) throws IOException {
                Map<String, String> sensorData = new HashMap<>();
                // Pattern의 matcher메서드를 사용해 문자열에 데이터에 대한 Matcher 객체생성
                // MATCHER 객체는 문자열 데이터에서 정규 표현식에 맞는 부분을 찾는데 사용
                // Pattern.compile로 학습시키고 matcher로 찾는거임 (group(1), group(2)) 사용
                Matcher matcher = pattern.matcher(data);
                while (matcher.find()) {
                    String sensorName = matcher.group(1);
                    String sensorValue = matcher.group(2);

                    String processedDataValue = DataProcessing.processData(sensorName ,sensorValue);
                    sensorData.put(sensorName, processedDataValue);
                }

                return sensorData;
            }
        }

    }

