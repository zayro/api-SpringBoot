package com.rest.api.example;


import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class HttpUnirest {

    public static void main(String[] args) {

        HttpUnirest httpUnirest = new HttpUnirest();
        httpUnirest.enviar();

    }


    public void enviar() {

        HttpResponse response = Unirest.post("http://localhost:4001/guardar")
                .header("Content-Type", "application/json")
                .body("{\r\n\t\"status\": \"succeeded\",\r\n\t\"createdDateTime\": \"2023-02-06T15:12:10Z\",\r\n\t\"lastUpdatedDateTime\": \"2023-02-06T15:12:12Z\",\r\n\t\"analyzeResult\": {\r\n\t\t\"version\": \"3.2.0\",\r\n\t\t\"modelVersion\": \"2022-04-30\"\r\n\t},\r\n\t\"asegurado\":{\r\n        \"nombre\": \"KAREN LORENA CASTAÑEDA CERON\",\r\n        \"email\":  \"ceron8125@gmail.com\",\r\n        \"telefono\": \"6012222222\",\r\n     \"pais\": \"57\",\r\n       \"departamento\": \"52\",\r\n        \"municipio\": \"401\"\r\n    }   }\r\n}")
                .asString();


        System.out.println("#############################");
        System.out.println("getBody()");
        System.out.println("#############################");
        System.out.println(response.getStatus());
        System.out.println(response.getBody());
        System.out.println("#############################");
    }


}
