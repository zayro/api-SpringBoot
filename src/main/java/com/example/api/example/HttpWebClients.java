package com.example.api.example;


import com.example.api.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public class HttpWebClients {

    @Autowired
    WebClient client = WebClient.create();

    private String OcpApimSubscriptionKey  = ConstantUtil.getPropertyConstant("OcpApimSubscriptionKey" );

    private String UrlOcr = ConstantUtil.getPropertyConstant("UrlOcr" );


    private final static Logger log = Logger.getLogger("com.example.api.example");


    public static void main(String[] args) {

        HttpWebClients httpWebClient = new HttpWebClients();
        httpWebClient.upload();

    }

       private final String IMAGE =   "./src/main/resources/files/unzip/zack.png";

    public void upload() {

        try {


            File load = new File(IMAGE);

            if(load.exists()){
                System.out.println("--- File Exist() ---");
            }

            // file to byte[], File -> Path
            byte[] someByteArray = Files.readAllBytes(load.toPath());

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

            ClientResponse responseHttp =  WebClient.builder()
                    .build()
                    .post()
                    .uri(UrlOcr)
                    .header("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(body))
                    .exchange()
                    .doOnSuccess(r -> {
                        System.out.println("########## doOnSuccess ############");
                        System.out.println(r.statusCode().value());
                        System.out.println(r.headers().asHttpHeaders() );
                        System.out.println( r.statusCode().getReasonPhrase());
                        System.out.println("############################");

                    }).doOnError(r -> {
                        System.out.println("############ doOnError ################");
                        System.out.println(r);
                        System.out.println("############################");

                    })
                    .block();


            System.out.println("********************************");
            System.out.println(responseHttp.statusCode().value());
            System.out.println(responseHttp.headers());
            System.out.println("********************************");





        }  catch (Exception e) {
            System.out.println("Parent Exception occurs" + e);
        }
    }


}
