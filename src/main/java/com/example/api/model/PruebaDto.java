package com.example.api.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class PruebaDto {

    @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String address;
    @Getter @Setter String phone;


//    public PruebaDto(String id, String name, String address, String phone) {
//        this.id = id;
//        this.name = name;
//        this.address = address;
//        this.phone = phone;
//    }
//
    public PruebaDto(String name, String address, String phone) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }


}
