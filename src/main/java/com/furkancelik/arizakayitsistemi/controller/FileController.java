package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.model.FileAttachment;
import com.furkancelik.arizakayitsistemi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/1.0/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/postAttachment")
    public FileAttachment savePostAttachment(MultipartFile file){
        try {
            return fileService.savePostAttachment(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
