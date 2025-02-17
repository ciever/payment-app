package com.example.accessing_data_mysql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Webhook {

    @Id
    private Long id = 1L;  // There should only be one webhook URL, hence the ID is fixed to 1

    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
