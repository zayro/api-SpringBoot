package com.example.api.example;



import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;


import java.nio.file.Files;

import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClient;

import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;

public class HttpWebClient {
    @Autowired
    private WebClient webClientBuilder ;

    @Autowired
    private WebClient webClient;

    private final static Logger log = Logger.getLogger("com.example.api.example");


    public static void main(String[] args) {

        HttpWebClient httpWebClient = new HttpWebClient();
        httpWebClient.upload();

    }

    private final String URL =   "https://ocr-pipo.cognitiveservices.azure.com/vision/v3.2/read/analyze";
    private final String IMAGE =   "./src/main/resources/files/unzip/zack.png";

    public void upload() {

        try {


            File load = new File(IMAGE);

            if (load.exists()) {
                System.out.println("--- File Exist() ---");
            }


            ClientResponse responseHttp =  WebClient.builder()
                    .build()
                    .post()
                    .uri(URL)
                    .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(fromFile(load)))
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


    public void uploadd() {

        try {


            File load = new File(IMAGE);

            if (load.exists()) {
                System.out.println("--- File Exist() ---");
            }

            String responseHttp =  webClient
                    .post()
                    .uri(URL)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(fromFile(load)))
                    .retrieve()
                    .bodyToMono(String.class).block();


            System.out.println(responseHttp);


        }  catch (Exception e) {
            System.out.println("Parent Exception occurs" + e);
        }
    }


    public Mono<String> getBody(){
        return webClient.post()
                .uri(URL)
                .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                .retrieve()
                .bodyToMono(String.class);
    }
    public Mono<HttpHeaders> getHeaders(){
        return webClient.post()
                .uri(URL)
                .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                .exchange()
                .map(response -> response.headers().asHttpHeaders());
    }

    private ExchangeFilterFunction logFilter() {
        return (clientRequest, next) -> {
            System.out.println("############################");
            System.out.println("External Request to {}" + clientRequest.url());
            System.out.println("############################");
            clientRequest.headers().forEach((name, values) -> {
                System.out.println("----------------------------------------");
                System.out.println("header: " + name + " with value " + values);
                System.out.println("----------------------------------------");
            });
            return next.exchange(clientRequest);
        };
    }

    public void upload_send() {

        webClient.post().uri("https://ocr-pipo.cognitiveservices.azure.com/vision/v3.2/read/analyze")
                .header("Ocp-Apim-Subscription-Key", "a28e4823b78f49e0af28c54905b5aff6")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters. fromMultipartData(fromFile(new File("HELP.md"))))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {

        try {

            System.out.println(MediaType.valueOf(Files.probeContentType(file.toPath())));
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", file, MediaType.valueOf("image/png"));
            return builder.build();
        }  catch (IOException e) {
            System.out.println("############ IOException ################" + e);

        } catch ( Exception e){
            System.out.println("############ Exception ################" + e);
        }

        return null;
    }

    final String urlServer="http://localhost:8081";


    public Mono<ResponseEntity<Mono<String>>> testGet(String param) {
        final long dateStarted = System.currentTimeMillis();
        WebClient webClient = WebClient.create(urlServer+"/server/");
        Mono<ClientResponse> respuesta = webClient.get().uri("?queryParam={name}", param).exchange();
        Mono<ClientResponse> respuesta1 = webClient.get().uri("?queryParam={name}","SPEED".equals(param)?"SPEED":"STOP").exchange();

        Mono<ResponseEntity<Mono<String>>> f1 = Mono.zip(respuesta, respuesta1)
                .map(t -> {
                    if (!t.getT1().statusCode().is2xxSuccessful()) {
                        return ResponseEntity.status(t.getT1().statusCode()).body(t.getT1().bodyToMono(String.class));
                    }
                    if (!t.getT2().statusCode().is2xxSuccessful()) {
                        return ResponseEntity.status(t.getT2().statusCode()).body(t.getT2().bodyToMono(String.class));
                    }
                    return ResponseEntity.ok().body(Mono.just(
                            "All OK. Seconds elapsed: " + (((double) (System.currentTimeMillis() - dateStarted) / 1000))));
                });
        return f1;
    }
}
