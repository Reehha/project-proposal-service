package com.example.projectproposal.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

  @NotBlank @Size(min = 2, max = 60)
  public String firstName;

  @NotBlank @Size(min = 1, max = 60)
  public String lastName;

  @NotNull @Past
  public LocalDate dob;

  @NotBlank @Email
  public String email;

  @NotBlank
  public String gender;

  @NotBlank @Size(min = 5, max = 25)
  public String phoneNumber;

  @NotBlank
  public String nationality;

  @NotBlank @Size(min = 5, max = 300)
  public String address;

  @NotBlank @Size(min = 8, max = 128)
  public String password;

  @NotBlank
  public String role;
}
