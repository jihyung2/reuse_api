package com.example.reuse_api.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/web")
    public String showSensorDataPage() {
        return "index"; // 뷰 이름 반환 (web.html 또는 해당하는 뷰 템플릿 파일 이름)
    }
}
