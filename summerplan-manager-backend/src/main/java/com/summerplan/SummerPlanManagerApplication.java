package com.summerplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SummerPlan Manager 主启动类
 */
@SpringBootApplication
@EnableJpaAuditing
public class SummerPlanManagerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SummerPlanManagerApplication.class, args);
    }
} 