package com.example.projectproposal.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.projectproposal.model.ProjectProposal;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class ProjectRepository {

  private final DynamoDbTable<ProjectProposal> table;

  public ProjectRepository(DynamoDbEnhancedClient enhanced) {
    this.table = enhanced.table("project", TableSchema.fromBean(ProjectProposal.class));
  }

  public void save(ProjectProposal p) {
    table.putItem(p);
  }

  public Optional<ProjectProposal> findById(String pid) {
    return Optional.ofNullable(table.getItem(r -> r.key(k -> k.partitionValue(pid))));
  }
}
