package com.example.reuse_api.controller;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class DataProcessing {
    public static String processData(String sensorName, String sensorValue) throws IOException {
        String[] parts = sensorName.split("\\.");

        if (parts.length == 2) {
            String dataType = parts[1]; // 두 번째 요소가 데이터 타입
            // dataType을 사용하여 처리
            if (dataType.equals("Analog")) {
                return sensorValue;

            } else if (dataType.equals("Binary")) {
                try {
                    byte[] binaryBytes = java.util.Base64.getDecoder().decode(sensorValue);
                    return binaryBytes.toString();
                }catch (IllegalArgumentException e){
                    System.err.println("바이너리 데이터 가공 실패: " + e.getMessage());
                    return null;
                }
            // 시리얼 통신을 통해 이미지를 전송하려면, 먼저 이미지를 바이트 배열로 변환해야 합니다.
            // 그리고 이 바이트 배열을 시리얼 통신을 통해 전송하고, 수신 측에서는 이 바이트 배열을 다시 이미지로 복원해야 합니다.
            } else if (dataType.equals("Image")) {
                try {
                    byte[] imageBytes = java.util.Base64.getDecoder().decode(sensorValue);
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    File outputFile = new File("../image_server/"+parts[0]+".jpeg");
                    ImageIO.write(image, "jpeg", outputFile); // 받은 파일을 이미지로 복원 후 저장
                    String storelocation = outputFile.getAbsolutePath(); // 이미지 파일의 절대 경로를 얻습니다.
                    return storelocation;

                } catch (IllegalArgumentException | IOException e) {
                // 디코딩 또는 이미지 처리 중에 오류가 발생한 경우 처리
                    System.err.println("이미지 데이터 가공 실패:" + e.getMessage());
                    return null;
                // 또는 다른 오류 처리 로직을 적용
            }

            } else if (dataType.equals("String")) {
                return sensorValue;
            }
            else if (dataType.equals("ASCII")) {
                String[] Ascii = sensorValue.split(",");
                // String Builer는 문자열을 연결해줌
                StringBuilder resultBuilder = new StringBuilder();
                for (String part : Ascii) {
                    int asciiValue = Integer.parseInt(part); // (아스키코드)문자열로 표현된 숫자를 정수로 변환
                    char character = (char) asciiValue; // char 캐스팅 연산자를 사용하여 정수값을 해당하는 ASCII 문자로 변환
                    resultBuilder.append(character);
                }
                return resultBuilder.toString();

            }
        }
        return sensorValue;
    }
}
