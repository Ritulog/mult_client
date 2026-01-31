package com.multi_client.aapwithdesign.design.factory;


import com.multi_client.aapwithdesign.design.formatter.MessageFormatter;
import com.multi_client.aapwithdesign.design.formatter.XmlMessageFormatter;

public class WebFormatterFactory implements MessageFormatterFactory {

    @Override
    public MessageFormatter getFormatter() {
        return new XmlMessageFormatter();
    }
}
