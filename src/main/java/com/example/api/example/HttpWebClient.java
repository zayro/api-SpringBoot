package com.example.api.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class WebClient {

    URI url = UriComponentsBuilder.fromHttpUrl(EXTERNAL_UPLOAD_URL).build().toUri();
    public static void main(String[] args) {

        Mono<HttpStatus> httpStatusMono = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_PDF)
                .body(BodyInserters.fromResource(resource))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(HttpStatus.class).thenReturn(response.statusCode());
                    } else {
                        throw new ServiceException("Error uploading file");
                    }
                });

    }
}
