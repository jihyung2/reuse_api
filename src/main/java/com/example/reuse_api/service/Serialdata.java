package com.example.reuse_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class Serialdata {
    private static final String SERIAL_PORT = "/dev/cu.usbserial-A10NLRJC";
    private static final String REST_API_SERVER = "http://127.0.0.1:8080/sensor";
    private static final int MAX_RETRY_COUNT = 5;
    private static final int RETRY_DELAY_MS = 2000;

    private StringBuilder buffer = new StringBuilder();

    public void readSerialData() {
        int retryCount = 0;
        while (true) {
            try {
                readFromSerialPort();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the serial port. Retrying...");
                retryCount++;
                if (retryCount > MAX_RETRY_COUNT) {
                    System.out.println("Failed to connect to the serial port after " + MAX_RETRY_COUNT + " attempts. Please check the connection.");
                    break;
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    private void readFromSerialPort() throws Exception {
        SerialPort serialPort = SerialPort.getCommPort(SERIAL_PORT);
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        System.out.println("Serial Communication Success!");

        while (true) {
            if (serialPort.bytesAvailable() > 0) {
                processDataFromSerialPort(serialPort);
            }
        }
    }

    private void processDataFromSerialPort(SerialPort serialPort) throws Exception {
        byte[] data = new byte[serialPort.bytesAvailable()];
        serialPort.readBytes(data, data.length);
        String receivedData = new String(data, "UTF-8");
        System.out.println("Received data: " + receivedData);
        buffer.append(receivedData);
        int startIndex = buffer.indexOf("$");
        int endIndex = buffer.indexOf("&");
        System.out.println("buffer: " + buffer.toString());
        if (startIndex >= 0 && endIndex > startIndex) {
            String message = buffer.substring(startIndex + 1, endIndex);
            sendToRestApi(message);
            buffer.delete(startIndex, endIndex + 1);
        }
    }

    private void sendToRestApi(String message) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("data", message);
        map.put("name", "UART data");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = null;
        try {
            jsonData = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(map);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_API_SERVER, requestEntity, String.class);
        if (response.getStatusCodeValue() == 200) {
            System.out.println("데이터 전송 성공");
        } else {
            System.out.println("데이터 전송 실패");
        }
    }
}
