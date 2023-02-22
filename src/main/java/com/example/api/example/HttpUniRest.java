package com.example.api.example;

import com.example.api.util.ConstantUtil;
import kong.unirest.*;
 import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class HttpUniRest {

    private String OcpApimSubscriptionKey  = ConstantUtil.getPropertyConstant("OcpApimSubscriptionKey" );

    private String UrlOcr = ConstantUtil.getPropertyConstant("UrlOcr" );
    public static void main(String[] args) {
        HttpUniRest httpUniRest = new HttpUniRest();
        httpUniRest.SendOcr();
    }


    public void SendOcr() {

        try {

            InputStream fileLoad = new FileInputStream("/ruta/prospectos.jpg");

            File load =  new File("/ruta/pcp_01.png");

            if(load.exists()){
                System.out.println("--- File Exist() ---");
            }

            HttpResponse response = Unirest.post(UrlOcr)
                    .header("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey)
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



    public void ReadOcr(){}


}
