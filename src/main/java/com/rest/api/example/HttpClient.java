package com.rest.api.example;

import org.apache.http.HttpEntity;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class HttpClient {

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient(); httpClient.GetInfo();

    }

    public void GetInfo() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpGet request = new HttpGet("http://localhost:4001/api/v1/query/demo" );

            // add request headers
            request.addHeader("Test", "pruebas");

            CloseableHttpResponse response = httpClient.execute(request);


            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity(); if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println("#####################################");
                System.out.println(result);
                System.out.println("#####################################");
            }

            response.close(); httpClient.close();
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
