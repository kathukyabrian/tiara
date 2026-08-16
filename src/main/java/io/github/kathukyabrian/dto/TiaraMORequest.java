package io.github.kathukyabrian.dto;

import lombok.Data;

@Data
public class TiaraMORequest {
    private String msgId;
    private String from;
    private String to;
    private String refId;
    private String message;
    private String linkId;
    private String receiveTime;
}
