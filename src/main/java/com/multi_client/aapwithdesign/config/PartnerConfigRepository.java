package com.multi_client.aapwithdesign.config;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartnerConfigRepository
        extends MongoRepository<PartnerConfig, String> {
}
