package com.example.reuse_api.service;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class Serialdata{

    public void readSerialData() {
        SerialPort serialPort = SerialPort.getCommPort("/dev/cu.usbserial-A10NLRJC"); // 시리얼 포트 경로 지정
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        System.out.println("DAD");
        try {
            while (true) {
                if (serialPort.bytesAvailable() > 0) {
                    byte[] data = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(data, data.length);

                    String receivedData = new String(data, "UTF-8");
                    System.out.println("Received data: " + receivedData);

                    // 받은 데이터 처리
                    RestTemplate restTemplate = new RestTemplate();

                    Map<String, String> map = new HashMap<>();
                    map.put("data", receivedData);
                    map.put("name", "None");

                    System.out.println("Received data: " + receivedData);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);

                    String restapi_server = "http://127.0.0.1:8080/sensor";
                    ResponseEntity<Map> responseFromPythonServer =
                            restTemplate.postForEntity(restapi_server, requestEntity, Map.class);
                    if (responseFromPythonServer.getStatusCodeValue() == 200){
                        System.out.println("데이터 전송 성공");
                    }
                    else {
                        System.out.println("데이터 전송 실패");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
