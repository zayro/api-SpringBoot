package com.example.api.repository;


import com.example.api.model.PruebaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class PruebaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int save(PruebaDto pruebaDto) {
        return jdbcTemplate.update("INSERT INTO demo.prueba (id, name, address, phone) VALUES(?,?,?,?)",
                new Object[]{pruebaDto.getId(), pruebaDto.getName(), pruebaDto.getAddress(), pruebaDto.getPhone() });
    }

    
    public int update(PruebaDto pruebaDto) {
        return jdbcTemplate.update("UPDATE demo.prueba SET name=?, address=?, phone=? WHERE id=?",
                new Object[]{pruebaDto.getId(), pruebaDto.getName(), pruebaDto.getAddress(), pruebaDto.getPhone() });
    }

    
    public PruebaDto findById(String id) {
        try {
            System.out.println("--------------------------------------------");
            System.out.println(" --w id:"+ id);
            System.out.println("--------------------------------------------");


            PruebaDto pruebaDto = jdbcTemplate.queryForObject("SELECT * FROM demo.prueba WHERE id=?",
                    BeanPropertyRowMapper.newInstance(PruebaDto.class), UUID.fromString(id));

            return pruebaDto;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }


    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM demo.prueba WHERE id=?", id);
    }

    
    public List<PruebaDto> findAll()  throws SQLException, ClassNotFoundException  {
        return jdbcTemplate.query("SELECT id, name, address, phone from demo.prueba ", BeanPropertyRowMapper.newInstance(PruebaDto.class));
    }

    
    public List<PruebaDto> findByName(boolean published) {
        return jdbcTemplate.query("SELECT * from demo.prueba WHERE name=?",
                BeanPropertyRowMapper.newInstance(PruebaDto.class), published);
    }

    
    public List<PruebaDto> findByNameContaining(String title) {
        String q = "SELECT * from demo.prueba WHERE name ILIKE '%" + title + "%'";

        return jdbcTemplate.query(q, BeanPropertyRowMapper.newInstance(PruebaDto.class));
    }

    

}



