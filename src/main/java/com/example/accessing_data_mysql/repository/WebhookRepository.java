package com.example.accessing_data_mysql.repository;

import com.example.accessing_data_mysql.model.Webhook;
import org.springframework.data.repository.CrudRepository;

public interface WebhookRepository extends CrudRepository<Webhook, Long> {
}
