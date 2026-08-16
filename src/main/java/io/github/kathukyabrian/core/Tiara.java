package io.github.kathukyabrian.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;
import io.github.kathukyabrian.dto.*;
import io.github.kathukyabrian.util.HttpUtil;
import okhttp3.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Tiara {
    private static final Logger logger = LogManager.getLogger(Tiara.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static SingleSMSResponse sendSingle(String recipient, String message, String refId) {
        logger.info("tiara|sending sms to phone number: {}|refId: {}", recipient, refId);
        if (refId == null) {
            refId = UUID.randomUUID().toString();
        }

        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();

        SingleSMSRequest singleSMSRequest = new SingleSMSRequest(
                applicationProperties.getSenderId(), recipient, message, refId
        );

        String apiKey = applicationProperties.getApiKey();

        try {
            String request = objectMapper.writeValueAsString(singleSMSRequest);

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + apiKey);

            String response = HttpUtil.post(applicationProperties.getSingleSMSEndpoint(), request, headerMap, MediaType.get("application/json; charset=utf-8"));

            TiaraSMSResponse tiaraSMSResponse = objectMapper.readValue(response, TiaraSMSResponse.class);

            return new SingleSMSResponse(tiaraSMSResponse, refId);
        } catch (IOException e) {
            return new SingleSMSResponse().fail(e, refId, recipient);
        }
    }

    public static List<SingleSMSResponse> sendBulk(List<SingleSMS> messages, String refId) {
        logger.info("tiara|sending sms to {} recipients|refId: {}", messages.size(), refId);
        if (refId == null) {
            refId = UUID.randomUUID().toString();
        }

        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();

        List<SingleSMSRequest> smsRequests = new ArrayList<>();
        for (SingleSMS singleSMS : messages) {
            SingleSMSRequest singleSMSRequest = new SingleSMSRequest(
                    applicationProperties.getSenderId(), singleSMS.getTo(), singleSMS.getMessage(), refId
            );
            smsRequests.add(singleSMSRequest);
        }

        String apiKey = applicationProperties.getApiKey();

        try {
            String request = objectMapper.writeValueAsString(smsRequests);

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + apiKey);

            String response = HttpUtil.post(applicationProperties.getBulkSMSEndpoint(), request, headerMap, MediaType.get("application/json; charset=utf-8"));

            List<TiaraSMSResponse> bulkSMSResponse = objectMapper.readValue(response, new TypeReference<List<TiaraSMSResponse>>() {
            });

            String finalRefId = refId;
            return bulkSMSResponse.stream().map(resp -> new SingleSMSResponse(resp, finalRefId))
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            return null;
        }
    }

    public static TiaraBalanceResponse getBalance() {
        logger.info("tiara|request to balance for current account");
        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + applicationProperties.getApiKey());

        try {
            String response = HttpUtil.get(applicationProperties.getCheckAccountBalanceEndpoint(), headerMap, MediaType.get("application/json; charset=utf-8"));
            return objectMapper.readValue(response, TiaraBalanceResponse.class);
        } catch (IOException ex) {
            return new TiaraBalanceResponse().fail(ex.getMessage());
        }
    }
}
