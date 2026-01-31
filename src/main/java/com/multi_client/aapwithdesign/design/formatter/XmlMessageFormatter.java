package com.multi_client.aapwithdesign.design.formatter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.multi_client.model.Message;

public class XmlMessageFormatter implements MessageFormatter {

    private final XmlMapper mapper = new XmlMapper();

    @Override
    public String format(Message message) throws Exception {
        return mapper.writeValueAsString(message);
    }

    @Override
    public String contentType() {
        return "application/xml";
    }
}
