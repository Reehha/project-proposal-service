package com.example.projectproposal.service;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.projectproposal.dto.ProjectRequest;
import com.example.projectproposal.model.ProjectProposal;
import com.example.projectproposal.repository.ProjectRepository;
import com.example.projectproposal.util.IdUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProjectService {

  private final ProjectRepository projects;
  private final ObjectMapper om;

  public ProjectService(ProjectRepository projects, ObjectMapper om) {
    this.projects = projects;
    this.om = om;
  }

  public ProjectProposal create(ProjectRequest req, String createdByUid) {
    validateTimelineJson(req.timelineJson);

    var p = new ProjectProposal();
    p.setPid(IdUtil.newProjectId());
    p.setPname(req.pname.trim());
    p.setTimelineJson(req.timelineJson.trim());
    p.setClientReq(mapItems(req.clientReq));
    p.setServices(mapItems(req.services));
    p.setPricingPlans(mapPlans(req.pricingPlans));
    p.setCreatedByUid(createdByUid);
    p.setCreatedAt(Instant.now());
    p.setUpdatedAt(Instant.now());

    projects.save(p);
    return p;
  }

  public ProjectProposal update(String pid, ProjectRequest req, String currentUid) {
    validateTimelineJson(req.timelineJson);

    var existing = projects.findById(pid)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));

    // simple ownership check (optional)
    if (existing.getCreatedByUid() != null && !existing.getCreatedByUid().equals(currentUid)) {
      throw new SecurityException("Not allowed");
    }

    existing.setPname(req.pname.trim());
    existing.setTimelineJson(req.timelineJson.trim());
    existing.setClientReq(mapItems(req.clientReq));
    existing.setServices(mapItems(req.services));
    existing.setPricingPlans(mapPlans(req.pricingPlans));
    existing.setUpdatedAt(Instant.now());

    projects.save(existing);
    return existing;
  }

  public ProjectProposal get(String pid, String currentUid) {
    var p = projects.findById(pid)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));

    if (p.getCreatedByUid() != null && !p.getCreatedByUid().equals(currentUid)) {
      throw new SecurityException("Not allowed");
    }
    return p;
  }

  private void validateTimelineJson(String json) {
    try {
      JsonNode node = om.readTree(json);
      if (!node.isArray() || node.size() != 6) {
        throw new IllegalArgumentException("timelineJson must be a JSON array with exactly 6 items");
      }
      for (JsonNode item : node) {
        if (!item.hasNonNull("month") || !item.hasNonNull("year") || !item.hasNonNull("desc")) {
          throw new IllegalArgumentException("Each timeline item must include month, year, desc");
        }
      }
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalArgumentException("timelineJson is not valid JSON");
    }
  }

  private java.util.List<ProjectProposal.Item> mapItems(java.util.List<ProjectRequest.ItemDto> items) {
    if (items == null) return new ArrayList<>();
    var out = new ArrayList<ProjectProposal.Item>();
    for (var i : items) {
      var x = new ProjectProposal.Item();
      x.setTitle(i.title.trim());
      x.setDesc(i.desc.trim());
      out.add(x);
    }
    return out;
  }

  private java.util.List<ProjectProposal.PricingPlan> mapPlans(java.util.List<ProjectRequest.PricingPlanDto> plans) {
    if (plans == null) return new ArrayList<>();
    var out = new ArrayList<ProjectProposal.PricingPlan>();
    for (var p : plans) {
      var x = new ProjectProposal.PricingPlan();
      x.setName(p.name.trim());
      x.setPrice(p.price.trim());
      x.setRecommended(Boolean.TRUE.equals(p.recommended));
      x.setFeatures(p.features);
      out.add(x);
    }
    // ensure at most 1 recommended (optional)
    long rec = out.stream().filter(pl -> Boolean.TRUE.equals(pl.getRecommended())).count();
    if (rec > 1) {
      throw new IllegalArgumentException("Only one pricing plan can be recommended=true");
    }
    return out;
  }
}
