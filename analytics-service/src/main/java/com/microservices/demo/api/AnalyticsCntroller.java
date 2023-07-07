package com.microservices.demo.api;

import com.microservices.demo.business.impl.AnalyticsService;
import com.microservices.demo.model.AnalyticsResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/")
public class AnalyticsCntroller {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsCntroller.class);

    private final AnalyticsService analyticsService;

    public AnalyticsCntroller(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/get-word-count-by-word/{word}")
    public @ResponseBody ResponseEntity<AnalyticsResponseModel> getWordCountbyWord(@PathVariable String word) {
        Optional<AnalyticsResponseModel> result = analyticsService.getWordAnalytics(word);
        if (result.isPresent()) {
            LOG.info("Analytics data returned with id {}", result.get().getId());
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.ok(AnalyticsResponseModel.builder().build());
    }

}
