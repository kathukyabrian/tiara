package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SingleSMSResponse {
    private String messageId;
    private String referenceId;
    private String to;
    private String status;
    private String statusCode;
    private String description;
    private String cost;
    private String balance;

    public SingleSMSResponse(TiaraSMSResponse response, String referenceId) {
        this.messageId = response.getMsgId();
        this.referenceId = referenceId;
        this.to = response.getTo();
        this.status = response.getStatus();
        this.statusCode = response.getStatusCode();
        this.description = response.getDesc();
        this.cost = response.getCost();
        this.balance = response.getBalance();
    }

    public SingleSMSResponse fail(Exception ex, String referenceId, String to) {
        SingleSMSResponse singleSMSResponse = new SingleSMSResponse();
        singleSMSResponse.setMessageId(null);
        singleSMSResponse.setReferenceId(referenceId);
        singleSMSResponse.setTo(to);
        singleSMSResponse.setStatus("FAILED");
        singleSMSResponse.setStatusCode("0");
        singleSMSResponse.setDescription(ex.getMessage());
        return singleSMSResponse;
    }
}
