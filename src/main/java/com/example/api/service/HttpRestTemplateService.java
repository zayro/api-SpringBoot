package com.example.api.service;

import com.example.api.exception.ApiRetryException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class HttpRestTemplateService {

    @Value("${OcpApimSubscriptionKey}")
    private String OcpApimSubscriptionKey ;
    @Value("${UrlOcr}")
    private String UrlOcr;
    private final String IMAGE =   "./src/main/resources/files/unzip/EPICRISIS.pdf";



    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory  = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(5000);
        clientHttpRequestFactory.setReadTimeout(5000);
        return clientHttpRequestFactory;
    }

    @Async
    public CompletableFuture<String> sendOcr()  {

        String header_url_ocr = "";

        try {
            File load =  new File(IMAGE);

            if(load.exists()){
                System.out.println("--- File Exist() ---");
            }

            // file to byte[], File -> Path
            byte[] someByteArray = Files.readAllBytes(load.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey);


            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            ContentDisposition contentDisposition = ContentDisposition
                    .builder("form-data")
                    .name("file")
                    .filename("FILES")
                    .build();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            fileMap.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(load.toPath()));
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(someByteArray, fileMap);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileEntity);

            HttpEntity<MultiValueMap<String, Object>> requestEntity  = new HttpEntity<>(body, headers);


            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate
                    .postForEntity(UrlOcr,  requestEntity, String.class);

            header_url_ocr = response.getHeaders().get("Operation-Location").get(0);

            System.out.println("--------------------------------------------");
            System.out.println(response.getHeaders().get("Operation-Location"));
            System.out.println("--------------------------------------------");

            return CompletableFuture.completedFuture(header_url_ocr);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }catch (Exception e) {

            System.out.println("--------------------------------------------");
            System.out.println("Exception ");
            System.out.println(e);
            System.out.println("--------------------------------------------");


        }

        return CompletableFuture.completedFuture(header_url_ocr);


    }

    //Si falla, se realizarán 5 intentos con 5 segundos de espera entre cada uno de ellos
    @Async
    @Retryable(value = {ApiRetryException.class}, maxAttempts = 5, backoff = @Backoff(5000))
    public CompletableFuture<String> GetOcr (String Url) {
        ResponseEntity<String> response = null;

        try {
            System.out.println("--------------------------------------------");
            System.out.println(Url);
            System.out.println("--------------------------------------------");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey);


            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

            response = restTemplate.exchange(Url, HttpMethod.GET, entity, String.class);


            System.out.println("--------------------------------------------");
            System.out.println("response.getBody(): ");
            System.out.println(response.getStatusCode());
            System.out.println("--------------------------------------------");

            JsonParser parser = new JsonParser();

            JsonElement jelement = parser.parse(response.getBody());

            JsonObject jobject = jelement.getAsJsonObject();


            System.out.println("--------------------------------------------");
            System.out.println(jobject.get("status"));
            System.out.println("--------------------------------------------");

            String status = jobject.get("status").getAsString();

            if (!status.equals("succeeded")) {
                System.out.println("--------------------------------------------");
                System.out.println(jobject.get("status"));
                System.out.println("--------------------------------------------");
                throw new ApiRetryException("Error in RetryExampleService.retryExample ");
            }

            System.out.println("--------------------------------------------");
            System.out.println("finish");
            System.out.println("--------------------------------------------");


        } catch (Exception e) {

            System.out.println("--------------------------------------------");
            System.out.println("Exception ");
            System.out.println(e);
            System.out.println("--------------------------------------------");
            e.printStackTrace();
            throw new RuntimeException(e);


        }

        return CompletableFuture.completedFuture(response.getBody().toString());
    }

    @Recover
    public String GetOcrRecovery(ApiRetryException t, String s) {
        System.out.println("Aqui el código que deseamos ejecutar en caso que la política de reintentos falle"+  t.getMessage());
        return "Retry Recovery OK!";
    }
    public JsonObject addOcr() {

        JsonObject data = new JsonObject();

        data.addProperty("name", "foo");
        data.addProperty("num", new Integer(100));
        data.addProperty("balance", new Double(1000.21));
        data.addProperty("is_vip", new Boolean(true));

        JsonObject obj = new JsonObject();

        obj.add("info", data);

        System.out.print(obj);

        return  obj;

    }


}
