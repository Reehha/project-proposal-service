package com.example.projectproposal.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.projectproposal.model.AppUser;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.Expression;

@Repository
public class UserRepository {

  private final DynamoDbTable<AppUser> table;

  public UserRepository(DynamoDbEnhancedClient enhanced) {
    this.table = enhanced.table("user", TableSchema.fromBean(AppUser.class));
  }

  public void save(AppUser user) {
    table.putItem(user);
  }

  public Optional<AppUser> findById(String uid) {
    return Optional.ofNullable(table.getItem(r -> r.key(k -> k.partitionValue(uid))));
  }

  /**
   * Simple scan to find by email (OK for small MVP).
   * For production: add a GSI on email and query instead of scan.
   */
  public Optional<AppUser> findByEmail(String emailLower) {
    Expression filter = Expression.builder()
        .expression("email = :e")
        .putExpressionValue(":e", AttributeValue.builder().s(emailLower).build())
        .build();

    PageIterable<AppUser> pages = table.scan(r -> r.filterExpression(filter));
    return pages.items().stream().findFirst();
  }
}
