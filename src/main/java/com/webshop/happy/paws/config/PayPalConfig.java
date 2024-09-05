package com.webshop.happy.paws.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfig {

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", "sandbox");
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        String clientId = "AVkAhOYaBciCGogGPIZRoR4rtCT-n0MzkCDIz-Tmv5K_OkLAPbfMBq4AnGM_YQpxpRu-o8JMe-iUXYzS";
        String clientSecret = "EL4ix8bCIIEMYGWKYqm46W8h_abUgcMoxyMIRc9vMLL__6SX5doP4Vjvxxg70sE1yA7LsM-hwGGp0swk";
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}