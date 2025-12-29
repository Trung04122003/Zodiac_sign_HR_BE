package com.jci.zodiac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SchedulingConfiguration - Enable Spring Scheduling
 * Required for @Scheduled annotations to work
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    // Spring will automatically detect and run all @Scheduled methods
}