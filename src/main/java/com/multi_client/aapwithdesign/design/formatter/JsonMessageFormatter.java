package com.multi_client.aapwithdesign.design.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi_client.model.Message;

public class JsonMessageFormatter implements MessageFormatter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String format(Message message) throws Exception {
        return mapper.writeValueAsString(message);
    }

    @Override
    public String contentType() {
        return "application/json";
    }
}
