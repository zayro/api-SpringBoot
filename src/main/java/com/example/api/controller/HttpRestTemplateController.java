package com.example.api.controller;

import com.example.api.service.HttpRestTemplateService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/ocr")
public class HttpRestTemplateController {

    @Autowired
    private HttpRestTemplateService httpRestTemplateService;

    @RequestMapping(value = "/read", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String read()   {
        CompletableFuture<String>  Url = httpRestTemplateService.sendOcr();


        CompletableFuture.allOf(Url).join();


        JsonParser parser = new JsonParser();

        String obj = "";


        try {
            //Thread.sleep(2000);
            CompletableFuture<String> response =  httpRestTemplateService.GetOcr(Url.get());

            while(!response.isDone()) {
                System.out.println("Reintentando ...");
                Thread.sleep(3000);
            }
            String dataResponse = response.get();

            JsonElement jelement = parser.parse(dataResponse);

            JsonObject  jobject = jelement.getAsJsonObject();

            JsonObject info = httpRestTemplateService.addOcr();

            jobject.add("usuario", info);



            obj = String.valueOf(jobject);



            System.out.println("--------------------------------------------");
            System.out.println(jobject);
            System.out.println("--------------------------------------------");

            System.out.println("--------------------------------------------");
            System.out.println(jobject.isJsonObject());
            System.out.println("--------------------------------------------");

            System.out.println("--------------------------------------------");
            System.out.println(obj);
            System.out.println("--------------------------------------------");

            return obj;

            
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
