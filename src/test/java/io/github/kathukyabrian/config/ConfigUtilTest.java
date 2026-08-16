package io.github.kathukyabrian.config;

import org.junit.jupiter.api.Test;
import io.github.kathukyabrian.constants.ServiceConstants;

import java.util.Properties;

public class ConfigUtilTest {
    @Test
    void testReadConfig() throws Exception {
        Properties properties = ConfigUtil.readConfig();
        assert properties.containsKey(ServiceConstants.SINGLE_SMS_ENDPOINT_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.BULK_SMS_ENDPOINT_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.CHECK_ACCOUNT_BALANCE_ENDPOINT_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.API_KEY_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.SENDER_ID_CONFIG_KEY);
    }

    @Test
    void testReadProperties() throws Exception {
        Properties properties = ConfigUtil.readConfig();
        ApplicationProperties applicationProperties = ConfigUtil.getProperties(properties);
        assert applicationProperties.getSingleSMSEndpoint() != null;
        assert applicationProperties.getBulkSMSEndpoint() != null;
//        assert applicationProperties.getCheckAccountBalanceEndpoint() != null;
        assert applicationProperties.getApiKey() != null;
//        assert applicationProperties.getCallbackUrl() != null;
//        assert applicationProperties.getMoUrl() != null;
        assert applicationProperties.getSenderId() != null;
    }
}
