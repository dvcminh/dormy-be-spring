package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytic")
public class AnalyticController {
    private final AnalyticService analyticService;

    //api to get User Growth Rate
    @GetMapping("/user")
    public double getUserGrowthRate() {
        return analyticService.calculateUserGrowthRate();
    }

    // Active Users Probability
    @GetMapping("/active-users")
    public double getActiveUsersProbability(@RequestParam UUID userId) {
        return analyticService.calculateActiveUserProbability(userId);
    }

    // Post Engagement Rate
    @GetMapping("/post-engagement")
    public double getPostEngagementRate(@RequestParam UUID userId) {
        return analyticService.calculatePostEngagementRate(userId);
    }
}
