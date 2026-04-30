package com.example.onboarding3.configuration;

import com.example.onboarding3.controllers.KafkaController;
import com.example.onboarding3.domain.Country;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Configuration
public class TestConfig {

//    @Bean
//    MockMvc mockMvc(Controller controller) {
//        return MockMvcBuilders.standaloneSetup(controller).build();
//    }
//
    @Bean
    MockMvc mockMvcKafka(KafkaController controller) {
        return MockMvcBuilders.standaloneSetup(controller).build();
    }

//    @Bean
//    TestRestTemplate restTemplateTest() {
//        return new TestRestTemplate();
//    }

    @Bean
    public KafkaProducer<String, Country> KafkaProducer(ConsumerFactory<String, Country> consumerFactory) {
        return (KafkaProducer<String, Country>) consumerFactory.createConsumer();
    }
}
