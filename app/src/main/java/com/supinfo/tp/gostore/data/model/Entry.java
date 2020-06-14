package com.supinfo.tp.gostore.data.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Entry implements Serializable {
    private String enter_time;
    private String out_time;
    private String status;
}
