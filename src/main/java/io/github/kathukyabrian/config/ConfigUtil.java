package io.github.kathukyabrian.config;

import io.github.kathukyabrian.constants.ServiceConstants;
import io.github.kathukyabrian.exceptions.ConfigurationException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigUtil {
    public static Properties readConfig() throws Exception {
        String fileName = System.getenv().get(ServiceConstants.TIARA_CONFIG_ENV_VARIABLE);

        Properties properties = new Properties();
        if (fileName != null) {
            properties.load(Files.newInputStream(Paths.get(fileName)));
        } else {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(ServiceConstants.DEFAULT_CONFIG_FILE_NAME));
        }

        return properties;
    }

    public static ApplicationProperties getProperties(Properties properties) {
        ApplicationProperties applicationProperties = new ApplicationProperties();

        applicationProperties.setSingleSMSEndpoint(properties.getProperty(ServiceConstants.SINGLE_SMS_ENDPOINT_CONFIG_KEY));
        applicationProperties.setBulkSMSEndpoint(properties.getProperty(ServiceConstants.BULK_SMS_ENDPOINT_CONFIG_KEY));
        applicationProperties.setCheckAccountBalanceEndpoint(properties.getProperty(ServiceConstants.CHECK_ACCOUNT_BALANCE_ENDPOINT_CONFIG_KEY));
        applicationProperties.setApiKey(properties.getProperty(ServiceConstants.API_KEY_CONFIG_KEY));
        applicationProperties.setSenderId(properties.getProperty(ServiceConstants.SENDER_ID_CONFIG_KEY));

        applicationProperties.setReadTimeout(Integer.valueOf(properties.getProperty(ServiceConstants.READ_TIMEOUT_CONFIG_KEY)));
        applicationProperties.setConnectTimeout(Integer.valueOf(properties.getProperty(ServiceConstants.CONNECT_TIMEOUT_CONFIG_KEY)));
        return applicationProperties;
    }

    public static void validateProperties(ApplicationProperties applicationProperties) throws ConfigurationException {
        String reason = null;

        if (applicationProperties.getSingleSMSEndpoint() == null || applicationProperties.getSingleSMSEndpoint().isEmpty()) {
            reason = ServiceConstants.SINGLE_SMS_ENDPOINT_CONFIG_KEY;
        }

        if (applicationProperties.getBulkSMSEndpoint() == null || applicationProperties.getBulkSMSEndpoint().isEmpty()) {
            reason = ServiceConstants.BULK_SMS_ENDPOINT_CONFIG_KEY;
        }

        // check account balance is not mandatory
//        if (applicationProperties.getCheckAccountBalanceEndpoint() == null || applicationProperties.getCheckAccountBalanceEndpoint().isEmpty()) {
//            reason = ServiceConstants.CHECK_ACCOUNT_BALANCE_ENDPOINT_CONFIG_KEY;
//        }

        if (applicationProperties.getApiKey() == null || applicationProperties.getApiKey().isEmpty()) {
            reason = ServiceConstants.API_KEY_CONFIG_KEY;
        }

        if (applicationProperties.getSenderId() == null || applicationProperties.getSenderId().isEmpty()) {
            reason = ServiceConstants.SENDER_ID_CONFIG_KEY;
        }

        if (reason != null) {
            throw new ConfigurationException(reason + " cannot be null or empty");
        }
    }
}
