package com.rest.api.service;

import com.rest.api.dao.PruebaDaoJdbc;
import com.rest.api.model.PruebaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PruebaService {

    @Autowired
    PruebaDaoJdbc pruebaDaoJdbc;


    public List<PruebaDTO> getDatos()   {

        try {
            return pruebaDaoJdbc.obtenerDatos();
        } catch (Exception e) {
            System.out.println("Error Getdatos");
            throw new RuntimeException(e);

        }

    }
}
