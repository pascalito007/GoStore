package com.supinfo.tp.gostore;

import lombok.Data;

@Data
public class CardInfo {
    private String cardNumber;
    private String cardHolder;
    private String expireDate;
    private String secretCode;
}
