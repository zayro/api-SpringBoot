package com.example.api.example;

import kong.unirest.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class HttpRequest {

    public static void main(String[] args) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.ReadOcr();
    }
    public void ReadOcrv1() {

        try {

       /*
        HttpResponse<String> response = Unirest.post("https://ocr-pipo.cognitiveservices.azure.com/vision/v3.2/read/analyze")
                .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                .field("file", new File("/Users/mzarias/Pictures/prospectos.jpg"))
                .asEmpty();
        */

            InputStream fileLoad = new FileInputStream("/ruta/prospectos.jpg");

            HttpResponse response = Unirest.post("https://ocr-pipo.cognitiveservices.azure.com/vision/v3.2/read/analyze")
                    .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                    .header("Content-Type", "application/octet-stream")
                    .field("file", new File("/ruta/pcp_01.png"))
                    .uploadMonitor((field, fileName, bytesWritten, totalBytes) -> {

                        System.out.println("loading()");
                        System.out.println((totalBytes - bytesWritten));
                    }).asString();
            //.field("file", fileLoad, "prospectos.jpg");




            System.out.println("#############################");
            System.out.println("getBody()");
            System.out.println("#############################");
            System.out.println(response.getBody());
            System.out.println("#############################");
            System.out.println("getHeaders()");
            System.out.println("#############################");
            System.out.println(response.getHeaders());
            System.out.println("#############################");
            System.out.println(response.getStatus());
            System.out.println("#############################");

        } catch (UnirestException e) {
            //Uh oh!
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            System.out.println("Parent Exception occurs");
        }


    }



    public void ReadOcr() {

        try {

            InputStream fileLoad = new FileInputStream("/ruta/prospectos.jpg");

            File load =  new File("/ruta/pcp_01.png");

            if(load.exists()){
                System.out.println("--- File Exist() ---");
            }

            HttpResponse response = Unirest.post("https://ocr-pipo.cognitiveservices.azure.com/vision/v3.2/read/analyze")
                    .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                    .field("file", load, Files.probeContentType(load.toPath()))
                    .uploadMonitor((field, fileName, bytesWritten, totalBytes) -> {
                System.out.println("loading()");
                System.out.println((totalBytes - bytesWritten));
            }).asString();



            System.out.println("#############################");
            System.out.println("getBody()");
            System.out.println("#############################");
            System.out.println(response.getBody());
            System.out.println("#############################");
            System.out.println("getHeaders()");
            System.out.println("#############################");

            System.out.println(response.getHeaders());

            List<String> nuevaURl = response.getHeaders().get("Operation-Location");
            System.out.println(nuevaURl);



            /*
            for (String element : nuevaURl.headers()){
                if (element.contains("Operation-Location")){
                    System.out.println(element);
                }
            }

             */
            System.out.println("#############################");
            System.out.println(response.getStatus());
            System.out.println("#############################");

        } catch (UnirestException e) {
            //Uh oh!
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            System.out.println("Parent Exception occurs"+ e);
        }


    }


    public void ReadOcrv2() {

        try {

            HttpResponse response = Unirest.post("https://eastus.api.cognitive.microsoft.com/vision/v3.2/read/analyze?language=es")
                    .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                    .header("Content-Type", "application/json")
                    .field("url", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Atomist_quote_from_Democritus.png/338px-Atomist_quote_from_Democritus.png")
                    .asJson();


            System.out.println("#############################");
            System.out.println("getBody()");
            System.out.println("#############################");
            System.out.println(response.getBody());
            System.out.println("#############################");
            System.out.println("getHeaders()");
            System.out.println("#############################");
            System.out.println(response.getHeaders());
            System.out.println("#############################");
            System.out.println(response.getStatus());
            System.out.println("#############################");

        } catch (UnirestException e) {
            //Uh oh!
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Parent Exception occurs");
        }


    }


}
