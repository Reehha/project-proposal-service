package com.example.projectproposal.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ProjectRequest {

  @NotBlank @Size(min = 2, max = 120)
  public String pname;

  /**
   * 6 month timeline JSON.
   * Expect an array of 6 items like:
   * [
   *  {"month":"Mar","year":2026,"desc":"..."},
   *  ...
   * ]
   */
  @NotBlank
  public String timelineJson;

  @Valid
  public List<ItemDto> clientReq;

  @Valid
  public List<ItemDto> services;

  @Valid
  public List<PricingPlanDto> pricingPlans;

  public static class ItemDto {
    @NotBlank @Size(max = 80)
    public String title;

    @NotBlank @Size(max = 500)
    public String desc;
  }

  public static class PricingPlanDto {
    @NotBlank @Size(max = 60)
    public String name;

    @NotBlank @Size(max = 40)
    public String price;

    public Boolean recommended = false;

    @NotEmpty
    public List<@NotBlank @Size(max = 80) String> features;
  }
}
