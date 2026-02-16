package com.example.projectproposal.model;

import java.time.LocalDate;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class AppUser {

  private String uid;
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private String email;
  private String gender;
  private String phoneNumber;
  private String nationality;
  private String address;
  private String passwordHash;
  private String role;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("uid")
  public String getUid() { return uid; }
  public void setUid(String uid) { this.uid = uid; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public LocalDate getDob() { return dob; }
  public void setDob(LocalDate dob) { this.dob = dob; }

  @DynamoDbAttribute("email")
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getGender() { return gender; }
  public void setGender(String gender) { this.gender = gender; }

  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public String getNationality() { return nationality; }
  public void setNationality(String nationality) { this.nationality = nationality; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  @DynamoDbAttribute("passwordHash")
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
}
