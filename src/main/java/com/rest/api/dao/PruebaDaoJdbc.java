package com.rest.api.dao;

import com.rest.api.model.PruebaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Repository
public class PruebaDaoJdbc  {


    @Resource(name = "sql")
    Properties sqlProperties;

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<PruebaDTO> obtenerDatos() {
        List<Map<String, Object>> results =  jdbcTemplate.queryForList(
                sqlProperties.getProperty("FindPrueba"));
        return buildList(results);
    }

    private List<PruebaDTO> buildList(List<Map<String, Object>> rs){
        List<PruebaDTO> lista = new ArrayList<>();
        for (Map<?, ?> row : rs) {
            PruebaDTO pruebaDTO = new PruebaDTO();
            pruebaDTO.setName((String) row.get("name"));
            pruebaDTO.setPhone((String) row.get("phone"));
            lista.add(pruebaDTO);
        }
        return lista;
    }
}
