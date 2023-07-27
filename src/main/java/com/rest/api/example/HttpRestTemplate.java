package com.rest.api.example;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;


import java.util.Map;

public class HttpRestTemplate {

    public static void main(String[] args) {

        HttpRestTemplate httpRestTemplate = new HttpRestTemplate();

        httpRestTemplate.enviar();

    }


    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(500000); clientHttpRequestFactory.setReadTimeout(500000);
        return clientHttpRequestFactory;
    }

    public void enviar() {

        try {
            HttpHeaders headers = new HttpHeaders();

            Gson gson = new Gson(); TypeToken<Map<String, String>> mapType = new TypeToken<Map<String, String>>() {};
            String json = "{\"status\": \"succeeded\",\"createdDateTime\": \"2023-02-06T15:12:10Z\"," +
                              "\"lastUpdatedDateTime\": \"2023-02-06T15:12:12Z\"}";

            String jsonString = "{}";

            Map map = gson.fromJson(jsonString, Map.class);


            Map<String, String> stringMap = gson.fromJson(json, mapType.getType());


            System.out.println(stringMap.get("status"));


            HttpEntity<Map> request = new HttpEntity<>(map, headers);


            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response =
                restTemplate.postForEntity("http://localhost:4001/guardar", request, String.class);


            System.out.println("#############################");
            System.out.println("getBody()");
            System.out.println("#############################");
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println("#############################");
        } catch (IllegalStateException | JsonSyntaxException exception) {

            System.out.println("#############################");
            System.out.println("exception()");
            System.out.println("#############################");
            System.out.println(exception);
            System.out.println("#############################");

        }

    }

}
