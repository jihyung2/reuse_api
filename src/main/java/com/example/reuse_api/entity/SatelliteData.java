package com.example.reuse_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sd1000u")
public class SatelliteData {
    @Id
    @Column(name="name")
    private String name;
    @Column(name = "data")
    private String data;

    public SatelliteData() {

    }

    public String getName(){return name;}
    public void setName(String name) {
        this.name = name;
    }

    public String getData(){return data;}
    public void setData(String data) {
        this.data = data;
    }
    public SatelliteData(String name, String data) {
        super();
        this.name = name;
        this.data = data;
    }
    // 생성자, getter 및 setter 메서드 추가
}
