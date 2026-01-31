package com.multi_client.aapwithdesign.route;

import com.multi_client.aapwithdesign.design.factory.FormatterFactoryResolver;
import com.multi_client.aapwithdesign.design.factory.MessageFormatterFactory;
import com.multi_client.aapwithdesign.design.formatter.MessageFormatter;
import com.multi_client.model.Message;
import com.multi_client.repository.MessageRepository;
import org.apache.camel.builder.RouteBuilder;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class MessageRoute extends RouteBuilder {

    private final MessageRepository repository;

    public MessageRoute(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void configure() {

        from("direct:getMessage")
            .process(exchange -> {

                //  ID trim and convert to ObjectId
                String idStr = exchange.getIn()
                        .getBody(String.class)
                        .trim();

                ObjectId objectId;
                try {
                    objectId = new ObjectId(idStr);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid MongoDB ObjectId: " + idStr);
                }

                //  clientType trim
                String clientType = exchange.getIn()
                        .getHeader("clientType", String.class);

                if (clientType == null || clientType.trim().isEmpty()) {
                    throw new RuntimeException("clientType header is missing");
                }
                clientType = clientType.trim();

                //  Fetch message from MongoDB
                Message message = repository.findById(objectId)
                        .orElseThrow(() -> new RuntimeException("Message not found"));

                //  Decide which Factory to use
                MessageFormatterFactory factory;

                if ("PARTNER".equalsIgnoreCase(clientType)) {
                    // Partner clients require additional config: responseFormat header
                    String partnerFormat = exchange.getIn()
                            .getHeader("responseFormat", String.class);

                    if (partnerFormat == null || partnerFormat.trim().isEmpty()) {
                        throw new RuntimeException("Partner responseFormat header is missing");
                    }

                    partnerFormat = partnerFormat.trim();

                    factory = FormatterFactoryResolver.resolve(clientType, partnerFormat);

                } else {
                    // MOBILE or WEB
                    factory = FormatterFactoryResolver.resolve(clientType);
                }

                //  Format message
                MessageFormatter formatter = factory.getFormatter();
                String response = formatter.format(message);

                //  Set response and content-type
                exchange.getMessage().setBody(response);
                exchange.getMessage().setHeader(
                        "Content-Type",
                        formatter.contentType()
                );
            });
    }
}
