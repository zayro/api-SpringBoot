package com.example.api.example;

import com.example.api.util.ConstantUtil;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;

import java.io.File;

public class HttpRestTemplate {


    private String OcpApimSubscriptionKey  = ConstantUtil.getPropertyConstant("OcpApimSubscriptionKey" );

    private String UrlOcr = ConstantUtil.getPropertyConstant("UrlOcr" );
    private final String IMAGE =   "./src/main/resources/files/unzip/zack.png";

    public static void main(String[] args) throws InterruptedException {
        HttpRestTemplate httpRestTemplate = new HttpRestTemplate();
        String url = httpRestTemplate.sendOcr();
        Thread.sleep(2000);
        httpRestTemplate.GetOcr(url);
    }

    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory  = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(500000);
        clientHttpRequestFactory.setReadTimeout(500000);
        return clientHttpRequestFactory;
    }

    public String sendOcr()  {

        String header_url_ocr = "";

        System.out.println("--------------------------------------------");
        System.out.println(UrlOcr);
        System.out.println(OcpApimSubscriptionKey);
        System.out.println("--------------------------------------------");

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

            return header_url_ocr;


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return header_url_ocr;


    }

    public ResponseEntity<String> GetOcr (String Url) {

        System.out.println("--------------------------------------------");
        System.out.println(Url);
        System.out.println("--------------------------------------------");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

         ResponseEntity<String> response  = restTemplate.exchange(Url,  HttpMethod.GET, requestEntity, String.class);

        System.out.println("--------------------------------------------");
        System.out.println("response.getBody(): ");
        System.out.println(response.getBody());
        System.out.println("--------------------------------------------");

        return response;

    }


}
