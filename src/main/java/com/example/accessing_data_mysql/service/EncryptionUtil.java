package com.example.accessing_data_mysql.service;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionUtil {

    private static final String ENCRYPTION_PASSWORD = "yourEncryptionPassword";  // password must match one from `application.properties`

    // Encrypt card number using Jasypt
    public String encrypt(String data) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(ENCRYPTION_PASSWORD);
        return encryptor.encrypt(data);  
    }

    // Decrypt card number using Jasypt, if viewing the card number is requried.
    public String decrypt(String encryptedData) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(ENCRYPTION_PASSWORD);
        return encryptor.decrypt(encryptedData);
    }
}
