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

        return sendSingleSMS(refId, recipient, applicationProperties.getApiKey(), applicationProperties.getSingleSMSEndpoint(), singleSMSRequest);
    }

    public static SingleSMSResponse sendSingle(String recipient, String message, String refId, String senderId, String apiKey) {
        logger.info("tiara|sending sms to phone number: {}|refId: {}", recipient, refId);
        if (refId == null) {
            refId = UUID.randomUUID().toString();
        }

        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();

        if (senderId == null || senderId.isEmpty()) {
            senderId = applicationProperties.getSenderId();
        }

        SingleSMSRequest singleSMSRequest = new SingleSMSRequest(
                senderId, recipient, message, refId
        );

        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = applicationProperties.getApiKey();
        }

        return sendSingleSMS(refId, recipient, apiKey, applicationProperties.getSingleSMSEndpoint(), singleSMSRequest);
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

        return sendBulkSMS(applicationProperties.getBulkSMSEndpoint(), refId, apiKey, smsRequests);
    }

    public static List<SingleSMSResponse> sendBulk(List<SingleSMS> messages, String refId, String senderId, String apiKey) {
        logger.info("tiara|sending sms to {} recipients|refId: {}", messages.size(), refId);
        if (refId == null) {
            refId = UUID.randomUUID().toString();
        }

        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();

        if (senderId == null || senderId.isEmpty()) {
            senderId = applicationProperties.getSenderId();
        }

        List<SingleSMSRequest> smsRequests = new ArrayList<>();
        for (SingleSMS singleSMS : messages) {
            SingleSMSRequest singleSMSRequest = new SingleSMSRequest(
                    senderId, singleSMS.getTo(), singleSMS.getMessage(), refId
            );
            smsRequests.add(singleSMSRequest);
        }


        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = applicationProperties.getApiKey();
        }

        return sendBulkSMS(applicationProperties.getBulkSMSEndpoint(), refId, apiKey, smsRequests);
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

    private static SingleSMSResponse sendSingleSMS(String refId, String recipient, String apiKey, String endpoint, SingleSMSRequest singleSMSRequest) {
        try {
            String request = objectMapper.writeValueAsString(singleSMSRequest);

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + apiKey);

            String response = HttpUtil.post(endpoint, request, headerMap, MediaType.get("application/json; charset=utf-8"));

            TiaraSMSResponse tiaraSMSResponse = objectMapper.readValue(response, TiaraSMSResponse.class);

            return new SingleSMSResponse(tiaraSMSResponse, refId);
        } catch (IOException e) {
            return new SingleSMSResponse().fail(e, refId, recipient);
        }
    }

    public static List<SingleSMSResponse> sendBulkSMS(String endpoint, String refId, String apiKey, List<SingleSMSRequest> smsRequests) {
        try {
            String request = objectMapper.writeValueAsString(smsRequests);

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + apiKey);

            String response = HttpUtil.post(endpoint, request, headerMap, MediaType.get("application/json; charset=utf-8"));

            List<TiaraSMSResponse> bulkSMSResponse = objectMapper.readValue(response, new TypeReference<List<TiaraSMSResponse>>() {
            });

            return bulkSMSResponse.stream().map(resp -> new SingleSMSResponse(resp, refId))
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            return null;
        }
    }
}
