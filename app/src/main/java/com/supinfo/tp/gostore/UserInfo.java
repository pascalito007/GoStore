package com.supinfo.tp.gostore;

import lombok.Data;

@Data
public class UserInfo {
    private String name;
    private String surname;
    private String  cardNumber;
    private String  privateCode;
    private String  expireDate;
    private String  email;
    private String  address;
}
