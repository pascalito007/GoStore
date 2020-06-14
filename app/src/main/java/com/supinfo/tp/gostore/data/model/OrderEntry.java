package com.supinfo.tp.gostore.data.model;

import lombok.Data;

@Data
public class OrderEntry {
    private String confirmed;
    private String date_time;
    private String product_id;
    private String qte;
    private String total_price;
    private String uniq_id;
    private String unit_price;
    private String user_id;

}
