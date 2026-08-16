package io.github.kathukyabrian.config;

import lombok.Data;

@Data
public class ApplicationProperties {
    private String singleSMSEndpoint;
    private String bulkSMSEndpoint;
    private String checkAccountBalanceEndpoint;
    private String apiKey;
    private String senderId;
}
