package com.multi_client.aapwithdesign.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "partner_config")
@Getter
@Setter
public class PartnerConfig {

    @Id
    private String partnerId;
    private String responseFormat; // JSON or XML

    // getters & setters
}
