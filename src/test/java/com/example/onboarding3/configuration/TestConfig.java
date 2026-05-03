package com.example.onboarding3.configuration;

import com.example.onboarding3.controllers.KafkaController;
import com.example.onboarding3.controllers.MongoController;
import com.example.onboarding3.controllers.RedisCacheController;
import com.example.onboarding3.domain.Country;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestConfig {

//    @Bean
//    MockMvc mockMvc(Controller controller) {
//        return MockMvcBuilders.standaloneSetup(controller).build();
//    }
//
//    @Bean
//    MockMvc mockMvcKafka(KafkaController kafkaController) {
//        return MockMvcBuilders.standaloneSetup(kafkaController).build();
//    }
//
//    @Bean
//    MockMvc mockMvcMongo(MongoController mongoController) {
//        return MockMvcBuilders.standaloneSetup(mongoController).build();
//    }
//
//    @Bean
//    MockMvc mockMvcRedis(RedisCacheController redisController) {
//        return MockMvcBuilders.standaloneSetup(redisController).build();
//    }

//    @Bean
//    TestRestTemplate restTemplateTest() {
//        return new TestRestTemplate();
//    }

//    @Bean
//    public KafkaProducer<String, Country> KafkaProducer(ProducerFactory<String, Country> producerFactory) {
//        return (KafkaProducer<String, Country>) producerFactory.createProducer();
//    }
}
