package com.example.accessing_data_mysql.service;

import com.example.accessing_data_mysql.model.Webhook;
import com.example.accessing_data_mysql.repository.WebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentWebhookService {
    private static final int MAX_RETRIES = 5; // 5 retries can be changed based on requirements
    private static final long RETRY_DELAY_MS = 2000; // 2 seconds delay can be changed based on requirements

    @Autowired
    private WebhookRepository webhookRepository;

    public void sendWebhookNotification(String paymentDetailsJson) {
        Webhook webhook = webhookRepository.findById(1L).orElse(null);
        if (webhook == null || webhook.getUrl() == null) {
            System.err.println("No webhook URL is set.");
            return;
        }

        String webhookUrl = webhook.getUrl();
        RestTemplate restTemplate = new RestTemplate();
        int retries = 0;

        while (retries < MAX_RETRIES) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, paymentDetailsJson, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Webhook sent successfully to " + webhookUrl);
                    return; // Success, exit
                } else {
                    throw new RuntimeException("Failed to send webhook. Status code: " + response.getStatusCode());
                }
            } catch (Exception e) {
                retries++;
                System.err.println("Error sending webhook, retrying... Attempt " + retries);
                try {
                    TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS * retries); // Exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.err.println("Failed to send webhook after " + MAX_RETRIES + " attempts.");
    }
}
