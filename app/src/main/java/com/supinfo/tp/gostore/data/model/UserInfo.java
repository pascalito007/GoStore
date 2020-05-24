package com.supinfo.tp.gostore.data.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserInfo implements Serializable {
    private String fullName;
    private String  cardNumber;
    private String  privateCode;
    private String  expireDate;
    private String  email;
    private String  address;
    private String  phone;
    private String cardHolder;
    private String secretCode;
    private String userName;
    private String password;
}
