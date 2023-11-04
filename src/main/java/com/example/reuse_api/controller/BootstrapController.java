package com.example.reuse_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BootstrapController {

    @RequestMapping("/index.html")
    public String Dashboard(){
        return "index";
    }
}
