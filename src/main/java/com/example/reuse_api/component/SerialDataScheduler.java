package com.example.reuse_api.component;

import com.example.reuse_api.service.Serialdata;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SerialDataScheduler {

    private Serialdata serialdata;

    public SerialDataScheduler(Serialdata serialdata) {
        this.serialdata = serialdata;
    }

    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void readSerialDataPeriodically() {
        serialdata.readSerialData();
    }
}
