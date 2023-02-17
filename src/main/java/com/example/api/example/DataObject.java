package com.example.api.example;

import kong.unirest.json.JSONObject;


public class DataObject {

    public static void main(String[] args) {
        DataObject dataObject = new DataObject();
        dataObject.addOcr();
    }

    public void addOcr() {

        JSONObject data = new JSONObject();

        data.put("nombre", "pepio perez");
        data.put("ciudad", "Bucaramanga");
        data.put("num", new Integer(100));
        data.put("balance", new Double(1000.21));
        data.put("is_vip", new Boolean(true));

        JSONObject obj = new JSONObject();

        obj.put("info", data);

        System.out.print(obj);

    }

}
