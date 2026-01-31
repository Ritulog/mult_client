package com.multi_client.aapwithdesign.design.factory;


import com.multi_client.aapwithdesign.design.formatter.JsonMessageFormatter;
import com.multi_client.aapwithdesign.design.formatter.MessageFormatter;

public class MobileFormatterFactory implements MessageFormatterFactory {

    @Override
    public MessageFormatter getFormatter() {
        return new JsonMessageFormatter();
    }
}
