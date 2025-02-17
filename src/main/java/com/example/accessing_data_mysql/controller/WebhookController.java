package com.example.accessing_data_mysql.controller;

import com.example.accessing_data_mysql.model.Webhook;
import com.example.accessing_data_mysql.repository.WebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private WebhookRepository webhookRepository;

    // Set the webhook URL (there can only be one)
    @PostMapping("/set")
    public ResponseEntity<String> setWebhook(@RequestBody Webhook webhook) {
        // Ensure only one webhook exists with id 1L
        Webhook existingWebhook = webhookRepository.findById(1L).orElse(new Webhook());
        existingWebhook.setUrl(webhook.getUrl());
        webhookRepository.save(existingWebhook);
        return new ResponseEntity<>("Webhook URL set successfully", HttpStatus.OK);
    }

    // Get the current webhook URL
    @GetMapping("/get")
    public ResponseEntity<String> getWebhook() {
        Webhook webhook = webhookRepository.findById(1L).orElse(null);
        if (webhook == null || webhook.getUrl() == null) {
            return new ResponseEntity<>("No webhook URL set", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(webhook.getUrl(), HttpStatus.OK);
    }
}
