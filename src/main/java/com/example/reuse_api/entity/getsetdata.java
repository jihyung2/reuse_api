package com.example.reuse_api.entity;


public class getsetdata {
    private String name;

    private String data;

    public getsetdata() {

    }

    public String getName(){return name;}
    public void setName(String name) {
        this.name = name;
    }

    public String getData(){return data;}
    public void setData(String data) {
        this.data = data;
    }

    public getsetdata(String name, String data) {
        super();
        this.name = name;
        this.data = data;
    }
    // 생성자, getter 및 setter 메서드 추가
}
