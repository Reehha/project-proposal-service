package com.example.projectproposal.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Configuration
public class DynamoConfig {

    @Value("${app.aws.region:us-east-1}")
    private String region;

    @Value("${app.dynamodb.endpoint:}")
    private String endpoint;

    @Value("${app.aws.accessKey:}")
    private String accessKey;

    @Value("${app.aws.secretKey:}")
    private String secretKey;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        var builder = DynamoDbClient.builder()
                .region(Region.of(region));

        // If access key and secret are provided, use them
        if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
            var creds = AwsBasicCredentials.create(accessKey, secretKey);
            builder.credentialsProvider(StaticCredentialsProvider.create(creds));
        } else {
            // Otherwise try default credentials chain (works on AWS services)
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        // Override endpoint if provided (useful for local development)
        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint.trim()));
        }

        return builder.build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}