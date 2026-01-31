package com.multi_client.aapwithdesign.design.formatter;


import com.multi_client.model.Message;

public interface MessageFormatter {
    String format(Message message) throws Exception;
    String contentType();
}
