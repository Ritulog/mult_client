package com.multi_client.aapwithdesign.design.factory;

import com.multi_client.aapwithdesign.design.formatter.JsonMessageFormatter;
import com.multi_client.aapwithdesign.design.formatter.MessageFormatter;
import com.multi_client.aapwithdesign.design.formatter.XmlMessageFormatter;

public class PartnerFormatterFactory implements MessageFormatterFactory {

    private final String responseFormat;

    public PartnerFormatterFactory(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    @Override
    public MessageFormatter getFormatter() {

        if ("XML".equalsIgnoreCase(responseFormat)) {
            return new XmlMessageFormatter();
        }

        return new JsonMessageFormatter(); // default
    }
}
