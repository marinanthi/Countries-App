package com.example.onboarding3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Onboarding3Application {

    public static void main(String[] args) {
        SpringApplication.run(Onboarding3Application.class, args);
    }
}
