package com.example.reuse_api.controller;

import com.example.reuse_api.entity.ImageData;
import com.example.reuse_api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
public class ImageDataController {

    @Autowired
    private ImageService imageDataService;

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<ImageData> imageDataOptional = imageDataService.getImageData(id);
        if(imageDataOptional.isPresent()) {
            ImageData imageData = imageDataOptional.get();
            response.getOutputStream().write(imageData.getData());
        }
    }
}