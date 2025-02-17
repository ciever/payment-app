package com.example.accessing_data_mysql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Payment {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  @NotEmpty(message = "First name must not be empty")
  private String firstName;

  @NotEmpty(message = "Last name must not be empty")
  private String lastName;

  @NotEmpty(message = "Zip code must not be empty")
  private String zipCode;

  @NotEmpty(message = "Card number must not be empty")
  private String cardNumber;

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }
 
}
