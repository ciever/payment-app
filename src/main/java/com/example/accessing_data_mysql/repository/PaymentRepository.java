package com.example.accessing_data_mysql.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.accessing_data_mysql.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    
}