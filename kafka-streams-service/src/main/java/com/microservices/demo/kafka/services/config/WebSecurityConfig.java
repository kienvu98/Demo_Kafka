package com.microservices.demo.kafka.services.config;

import com.microservices.demo.kafka.services.security.KafkaStreamsUserDetailService;
import com.microservices.demo.kafka.services.security.KafkaStreamsUserJwtConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final KafkaStreamsUserDetailService kafkaStreamsUserDetailService;

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Value("${security.paths-to-ignore}")
    private String[] pathsToIgnore;

    public WebSecurityConfig(KafkaStreamsUserDetailService kafkaStreamsUserDetailService,
                             OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        this.kafkaStreamsUserDetailService = kafkaStreamsUserDetailService;
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(kafaStreamsUserJwtAuthConverter());
    }

    @Override
    public void configure(WebSecurity webSecurity){
        webSecurity
                .ignoring()
                .antMatchers(pathsToIgnore);
    }

    @Bean
    Converter<Jwt,? extends AbstractAuthenticationToken> kafaStreamsUserJwtAuthConverter() {
        return new KafkaStreamsUserJwtConverter(kafkaStreamsUserDetailService);
    }

    JwtDecoder jwtDecoder(@Qualifier("kafka-streams-service-audience-validator")
                          OAuth2TokenValidator<Jwt> oAuth2TokenValidator) {
        NimbusJwtDecoder jwtDecoder =
                (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, oAuth2TokenValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }
}
