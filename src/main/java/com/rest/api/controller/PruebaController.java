package com.rest.api.controller;

import com.rest.api.model.PruebaDTO;
import com.rest.api.service.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/prueba")
public class PruebaController {

    @Autowired
    private PruebaService service;

    @GetMapping(value = "/test")
    public ResponseEntity<List<PruebaDTO>> obtenerMotivoReclamacion( ) {
        return new ResponseEntity(service.getDatos(), HttpStatus.OK);
    }
}
