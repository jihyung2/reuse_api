package com.example.reuse_api.service;
import com.fazecast.jSerialComm.SerialPort;
import jakarta.annotation.PostConstruct;
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
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class Serialdata {

    private StringBuilder buffer = new StringBuilder();


    public void readSerialData() {
        SerialPort serialPort = SerialPort.getCommPort("/dev/cu.usbserial-A10NLRGZ"); // 시리얼 포트 경로 지정
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        System.out.println("Serial Communication Success!");


        try {
            while (true) {
                if (serialPort.bytesAvailable() > 0) {
                    byte[] data = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(data, data.length);

                    String receivedData = new String(data, "UTF-8");

                    System.out.println("Received data: " + receivedData);

                    buffer.append(receivedData); //문자열 버퍼(문자열 누적기) 수신 받은 데이터 누적
                    // 차례차례 처리하는 코드임, 하나의 한 세트($,&) 처리
                    int startIndex = buffer.indexOf("$");
                    int endIndex = buffer.indexOf("&");
                    System.out.println("buffer: " + buffer.toString());
                    if (startIndex >= 0 && endIndex > startIndex){ //&가 없으면 -1 출력함, end>start 는 $시작해서 &끝나는 메세지만 있을때 처리
                         // 받은 데이터 처리
                        String message = buffer.substring(startIndex + 1, endIndex);

                        RestTemplate restTemplate = new RestTemplate();

                        Map<String, String> map = new HashMap<>();

                        map.put("data", message);
                        map.put("name", "UART data");

                        //map을 json 형태로 보내려면 objectMapper을 해줘야한다.
                        ObjectMapper objectMapper = new ObjectMapper();
                        String jsonData = objectMapper.writeValueAsString(map);

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        System.out.println(map);

                        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);
                        String restapi_server = "http://127.0.0.1:8080/sensor";

                        ResponseEntity<String> response = restTemplate.postForEntity(restapi_server, requestEntity, String.class);

                        if (response.getStatusCodeValue() == 200){
                            System.out.println("데이터 전송 성공");
                        }
                        else {
                            System.out.println("데이터 전송 실패");
                        }
                        buffer.delete(startIndex, endIndex + 1); //$, & 안에있는 데이터 썼으니 지움

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
