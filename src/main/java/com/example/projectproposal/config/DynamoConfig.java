package com.example.projectproposal.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Configuration
public class DynamoConfig {

  @Value("${app.aws.region}")
  private String region;

  @Value("${app.dynamodb.endpoint:}")
  private String endpoint;

  @Value("${app.aws.accessKey}")
  private String accessKey;

  @Value("${app.aws.secretKey}")
  private String secretKey;

  @Bean
  public DynamoDbClient dynamoDbClient() {

    var creds = AwsBasicCredentials.create(accessKey, secretKey);

    var builder = DynamoDbClient.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(creds));

    if (endpoint != null && !endpoint.isBlank()) {
      builder.endpointOverride(URI.create(endpoint.trim()));
    }

    return builder.build();
  }

  @Bean
  public DynamoDbEnhancedClient enhancedClient(DynamoDbClient dynamoDbClient) {
    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build();
  }
}
