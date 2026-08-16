package io.github.kathukyabrian.dto;

import lombok.Data;

@Data
public class TiaraSMSResponse {
    private String cost;
    private String mnc;
    private String balance;
    private String msgId;
    private String to;
    private String mcc;
    private String desc;
    private String status;
    private String statusCode;
}
