package com.poly.controller;

import com.poly.dto.ImageDto;
import com.poly.entity.User;
import com.poly.ex.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class BucketController {

    @Autowired
    private AmazonClient amazonClient;

    @PostMapping("/uploadFile")
    public ImageDto uploadFile(@ModelAttribute User user ,@ModelAttribute MultipartFile file) {
        return amazonClient.uploadFile(file);
    }
}
