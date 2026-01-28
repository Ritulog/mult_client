package com.multi_client.controller;

import com.multi_client.model.Message;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final ProducerTemplate producerTemplate;

    public MessageController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }


    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        return producerTemplate.requestBody(
                "direct:createMessage",
                message, Message.class);
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
