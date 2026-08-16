package io.github.kathukyabrian.dto;

import lombok.Data;

@Data
public class TiaraSMSCallback {
    private String msgId;
    private String from;
    private String to;
    private String refId;
    private String status;
    private String statusReason;
    private String deliveryTime;
}
