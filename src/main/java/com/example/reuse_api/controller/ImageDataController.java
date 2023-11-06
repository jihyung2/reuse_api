package com.example.reuse_api.controller;

import com.example.reuse_api.entity.ImageData;
import com.example.reuse_api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ImageDataController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/image-ids")
    public @ResponseBody List<Long> getImageIds() {
        return imageService.getAllImageIds();
    }
}