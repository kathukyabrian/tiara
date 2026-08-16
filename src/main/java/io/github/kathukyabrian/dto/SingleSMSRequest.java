package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SingleSMSRequest {
    private String from;
    private String to;
    private String message;
    private String refId;
}
