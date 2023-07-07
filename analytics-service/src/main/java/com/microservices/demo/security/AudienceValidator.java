package com.microservices.demo.security;

import com.microservices.demo.config.AnalyticsServiceConfigData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Qualifier(value = "analytics-service-audience-validator")
@Component
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final AnalyticsServiceConfigData analyticsServiceConfigData;

    public AudienceValidator(AnalyticsServiceConfigData analyticsServiceConfigData) {
        this.analyticsServiceConfigData = analyticsServiceConfigData;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience().contains(analyticsServiceConfigData.getCustomAudience())) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error auth2Error = new OAuth2Error("invalid_token",
                "The required audience" + analyticsServiceConfigData.getCustomAudience() + "is missing !", null);
        return OAuth2TokenValidatorResult.failure(auth2Error);
    }
}
