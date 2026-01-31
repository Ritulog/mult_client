package com.multi_client.aapwithdesign.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final ProducerTemplate producerTemplate;

    public MessageController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @GetMapping("/{id}")
    public Object getMessage(
            @PathVariable String id,
            @RequestHeader("clientType") String clientType) {

        return producerTemplate.requestBodyAndHeader(
                "direct:getMessage",
                id,
                "clientType",
                clientType
        );
    }

}
