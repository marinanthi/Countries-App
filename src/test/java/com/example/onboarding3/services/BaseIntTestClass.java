package com.example.onboarding3.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
public abstract class BaseIntTestClass {
    @Container @ServiceConnection
    static MongoDBContainer mongo =
            new MongoDBContainer("mongo:7");

    @Container @ServiceConnection
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("docker.io/redis/redis-stack"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
//        System.out.println("DynamicPropertySource CALLED");
//        System.out.println(mongo.getHost());
//        System.out.println(mongo.getReplicaSetUrl());
//        System.out.println(mongo.getFirstMappedPort());
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort());
//        registry.add("spring.mongodb.uri", mongo::getReplicaSetUrl);
    }
}
