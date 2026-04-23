package com.example.onboarding3.configuration;

import com.example.onboarding3.domain.Country;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .build();
    }

    @Bean
    public KafkaConsumer<String, Country> kafkaConsumer(ConsumerFactory<String, Country> consumerFactory) {
        return (KafkaConsumer<String, Country>) consumerFactory.createConsumer();
    }

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name("new-topic")
                .partitions(2)
                .replicas(1)
                .config("retension.ms", "10000")
                .build();
    }
}