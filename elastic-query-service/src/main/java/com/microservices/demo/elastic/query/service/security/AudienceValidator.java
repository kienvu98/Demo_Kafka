package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.config.ElasticQuerySericeConfigData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Qualifier("elastic-query-service-audience-validator")
@Component
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final ElasticQuerySericeConfigData elasticQuerySericeConfigData;

    public AudienceValidator(ElasticQuerySericeConfigData configData) {
        this.elasticQuerySericeConfigData = configData;
    }

    // method xac thuc lay Jwt vaf tra ve Oauth2Token
    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience().contains(elasticQuerySericeConfigData.getCustomAudience())) {
            return OAuth2TokenValidatorResult.success();
        } else {
            OAuth2Error auth2Error = new OAuth2Error("invalid_token", "the required audience" + elasticQuerySericeConfigData.getCustomAudience()
                                                    + "is missing", null);
            return OAuth2TokenValidatorResult.failure(auth2Error);
        }
    }
}
