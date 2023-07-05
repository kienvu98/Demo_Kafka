package com.microservices.demo.elastic.query.service.config;

import com.microservices.demo.elastic.query.service.security.TiwtterUserJwtConverter;
import com.microservices.demo.elastic.query.service.security.TwitterQueryUserDetailService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TwitterQueryUserDetailService twitterQueryUserDetailService;

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    public WebSecurityConfig(TwitterQueryUserDetailService twitterQueryUserDetailService,
                             OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        this.twitterQueryUserDetailService = twitterQueryUserDetailService;
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    }

    @Value("${security.paths-to-ignore}")
    private String[] pathsToIgnore;

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
                .jwtAuthenticationConverter(tiwtterQueryUserJwtConverter());
    }

    @Bean
    Converter<Jwt,? extends AbstractAuthenticationToken> tiwtterQueryUserJwtConverter() {
        return new TiwtterUserJwtConverter(twitterQueryUserDetailService);
    }

    @Bean
    JwtDecoder jwtDecoder(@Qualifier("elastic-query-service-audience-validator")
                          OAuth2TokenValidator<Jwt> audienceValidator) {
        NimbusJwtDecoder jwtDecoder =(NimbusJwtDecoder) JwtDecoders
                .fromOidcIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.
                createDefaultWithIssuer(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(pathsToIgnore);
    }

    // mã hóa mật khẩu
    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
