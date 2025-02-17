package com.example.accessing_data_mysql.controller;

import com.example.accessing_data_mysql.service.PaymentWebhookService;
import com.example.accessing_data_mysql.service.EncryptionUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import com.example.accessing_data_mysql.model.Payment;
import com.example.accessing_data_mysql.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This class is a Controller
@RequestMapping(path="/payment") // URL's start with /payment
public class MainController {

  @Autowired // Auto-injection of PaymentRepository
  private PaymentRepository paymentRepository;

  @Autowired
  private PaymentWebhookService paymentWebhookService;

  @Autowired
  private EncryptionUtil encryptionUtil;  // Inject EncryptionUtil for encryption/decryption
 
  // Post Mapping to create a new Payment
  @PostMapping(path="/add")
  public @ResponseBody ResponseEntity<Object> addNewPayment(@Valid @RequestBody Payment payment) {
      try {
        // Validate card number is 16 digits before encryption (only user input)
        String cardNumber = payment.getCardNumber();

        // Validate the card number manually to ensure it's 16 digits. Wanted to do this in model. But the validation of 16 digits max, while the enctypted value is up to 255 made it difficuilt. I would do further reasearch to fix this.
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return new ResponseEntity<>("Card number must be a valid 16-digit number", HttpStatus.BAD_REQUEST);
        }

        // Encrypt the card number before saving it to the database
        String encryptedCardNumber = encryptionUtil.encrypt(cardNumber);  // Use Jasypt to encryption card number
        payment.setCardNumber(encryptedCardNumber);  // Set the encrypted card number
      } catch (Exception e) {
        System.err.println("Error during encryption: " + e.getMessage());
        return new ResponseEntity<>("Encryption failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

      // Save the payment to the repository
      paymentRepository.save(payment);

      String paymentDetailsJson = "{ \"firstName\": \"" + payment.getFirstName() + "\", \"lastName\": \"" + payment.getLastName() + "\" }";
      // Call the PaymentWebhookService to send the payment details
      paymentWebhookService.sendWebhookNotification(paymentDetailsJson);

      // Return success response with the ID of the created payment
      return ResponseEntity.status(HttpStatus.CREATED).body("Payment saved successfully. ID: " + payment.getId());
  }

  // Get Mapping to return all Payments
  @GetMapping(path="/all")
  public @ResponseBody Iterable<Payment> getAllPayments() {
    return paymentRepository.findAll();
  }
}
