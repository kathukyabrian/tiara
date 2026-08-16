package io.github.kathukyabrian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TiaraBalanceResponse {
    private String balance;
    private String currency;
    private String statusCode;
    private String status;
    private String desc;
    private String timestamp;

    public TiaraBalanceResponse fail(String failureReason) {
        TiaraBalanceResponse tiaraBalanceResponse = new TiaraBalanceResponse();
        tiaraBalanceResponse.setBalance(null);
        tiaraBalanceResponse.setCurrency(null);
        tiaraBalanceResponse.setStatusCode("1");
        tiaraBalanceResponse.setStatus("FAILED");
        tiaraBalanceResponse.setDesc(failureReason);
        tiaraBalanceResponse.setTimestamp(LocalDateTime.now().toString());
        return tiaraBalanceResponse;
    }
}

