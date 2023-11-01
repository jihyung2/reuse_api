package com.example.reuse_api.controller;

public class SatelliteData {
    private String name;
    private String data;

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
