package com.example.api.controller;


import com.example.api.service.UnZip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/zip")
public class ZipController {

    @Autowired
    private UnZip unZip;


    @GetMapping("/unzip")
    public List<String> unZipGet(@RequestParam String archivoZip, @RequestParam String rutaSalida){
        return unZip.unZipFile(archivoZip, rutaSalida);
    }

    @PostMapping("/unzip")
    public List<String> unZipPost(@RequestBody  String archivoZip, @RequestBody  String rutaSalida){
        return unZip.unZipFile(archivoZip, rutaSalida);
    }





}
