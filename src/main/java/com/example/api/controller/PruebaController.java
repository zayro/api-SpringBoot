package com.example.api.controller;

import com.example.api.model.PruebaDto;
import com.example.api.repository.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PruebaController {


    @Autowired
    PruebaRepository pruebaRepository;

    //@GetMapping("/tutorials")
    @RequestMapping(value = "/tutorials", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<PruebaDto>> getAllTutorials(@RequestParam(required = false) String title) {

        System.out.println("--- /tutorials/ ---");
        try {
            List<PruebaDto> tutorials = new ArrayList<PruebaDto>();

            if (title == null)
                pruebaRepository.findAll().forEach(tutorials::add);
            else
                pruebaRepository.findByNameContaining(title).forEach(tutorials::add);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("********************************");
            System.out.println(e);
            System.out.println("********************************");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@GetMapping("/tutorialse/{id}/")
    @RequestMapping(value = "/tutorials/{id}", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<PruebaDto> getTutorialById(@PathVariable("id") String id) {

        System.out.println("--- /tutorials/{id} ---");


        try {
            PruebaDto tutorial = pruebaRepository.findById(id);

            if (tutorial != null) {
                return new ResponseEntity<>(tutorial, HttpStatus.OK);
            } else {
                System.out.println("--- no se econtraron registros ---");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("********************************");
            System.out.println(e);
            System.out.println("********************************");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<String> createTutorial(@RequestBody PruebaDto tutorial) {
        try {
            pruebaRepository.save(new PruebaDto(tutorial.getName(), tutorial.getAddress(), tutorial.getPhone()));
            return new ResponseEntity<>("PruebaDto was created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<String> updateTutorial(@PathVariable("id") String id, @RequestBody PruebaDto tutorial) {
        PruebaDto _tutorial = pruebaRepository.findById(id);

        if (_tutorial != null) {
            _tutorial.setId(id);
            _tutorial.setName(tutorial.getName());
            _tutorial.setAddress(tutorial.getAddress());
            _tutorial.setPhone(tutorial.getPhone());

            pruebaRepository.update(_tutorial);
            return new ResponseEntity<>("PruebaDto was updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find PruebaDto with id=" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable("id") long id) {
        try {
            int result = pruebaRepository.deleteById(id);
            if (result == 0) {
                return new ResponseEntity<>("Cannot find PruebaDto with id=" + id, HttpStatus.OK);
            }
            return new ResponseEntity<>("PruebaDto was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete tutorial.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<PruebaDto>> findByName() {

        System.out.println("--- /tutorials/published ---");
        try {
            List<PruebaDto> tutorials = pruebaRepository.findByName(true);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}
