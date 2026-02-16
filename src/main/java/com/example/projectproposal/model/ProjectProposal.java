package com.example.projectproposal.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class ProjectProposal {

  private String pid;
  private String pname;

  // Stored as a JSON string to keep DynamoDB mapping simple
  private String timelineJson;

  private List<Item> clientReq = new ArrayList<>();
  private List<Item> services = new ArrayList<>();
  private List<PricingPlan> pricingPlans = new ArrayList<>();

  private String createdByUid;
  private Instant createdAt;
  private Instant updatedAt;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("pid")
  public String getPid() { return pid; }
  public void setPid(String pid) { this.pid = pid; }

  public String getPname() { return pname; }
  public void setPname(String pname) { this.pname = pname; }

  @DynamoDbAttribute("timelineJson")
  public String getTimelineJson() { return timelineJson; }
  public void setTimelineJson(String timelineJson) { this.timelineJson = timelineJson; }

  public List<Item> getClientReq() { return clientReq; }
  public void setClientReq(List<Item> clientReq) { this.clientReq = clientReq; }

  public List<Item> getServices() { return services; }
  public void setServices(List<Item> services) { this.services = services; }

  public List<PricingPlan> getPricingPlans() { return pricingPlans; }
  public void setPricingPlans(List<PricingPlan> pricingPlans) { this.pricingPlans = pricingPlans; }

  public String getCreatedByUid() { return createdByUid; }
  public void setCreatedByUid(String createdByUid) { this.createdByUid = createdByUid; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

  @DynamoDbBean
  public static class Item {
    private String title;
    private String desc;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
  }

  @DynamoDbBean
  public static class PricingPlan {
    private String name;
    private String price;
    private Boolean recommended;
    private List<String> features = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public Boolean getRecommended() { return recommended; }
    public void setRecommended(Boolean recommended) { this.recommended = recommended; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
  }
}
