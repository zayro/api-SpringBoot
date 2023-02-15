package com.example.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class UploadController {

    @GetMapping(value = "/saludar")
    public String saludar() {

        return "Hola mundo";

    }

}
