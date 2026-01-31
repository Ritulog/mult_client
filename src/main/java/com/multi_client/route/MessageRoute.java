package com.multi_client.route;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.multi_client.model.Message;
import com.multi_client.repository.MessageRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageRoute extends RouteBuilder {

    private final MessageRepository repository;

    public MessageRoute(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void configure() {

//        onException(RuntimeException.class)
//                .handled(true)
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
//                .setBody(constant("Message not found"));

        //post
        from("direct:createMessage")
                .log("createMessage route HIT with body: ${body}")
                .bean(repository, "save");


        // get
//        from("direct:getMessage")
//                .process(exchange -> {
//                    String idStr = exchange.getIn().getBody(String.class);
//
//                    System.out.println("RAW ID = [" + idStr + "]");
//                    System.out.println("ID LENGTH = " + (idStr == null ? 0 : idStr.length()));
//
//                    ObjectId objectId = new ObjectId(idStr.trim());
//
//                    Message message = repository.findById(objectId)
//                            .orElseThrow(() -> new RuntimeException("Message not found"));
//
//                    exchange.getMessage().setBody(message);
//                });
//    }

        from("direct:getMessage")
                .log("Fetching message with id: ${body}")
                .process(exchange -> {

                    String idStr = exchange.getIn().getBody(String.class);
                    String clientType = exchange.getIn().getHeader("clientType", String.class);

                    System.out.println("RAW ID = [" + idStr + "]");
                    System.out.println("ID LENGTH = " + (idStr == null ? 0 : idStr.length()));

                    if (idStr == null || !ObjectId.isValid(idStr.trim())) {
                        throw new RuntimeException("Invalid Message ID");
                    }

                    ObjectId objectId = new ObjectId(idStr.trim());

                    Message message = repository.findById(objectId)
                            .orElseThrow(() -> new RuntimeException("Message not found"));

                    ObjectMapper jsonMapper = new ObjectMapper();
                    XmlMapper xmlMapper = new XmlMapper();

                    // 🔥 SAME OLD CLIENT LOGIC (JSON / XML)
                    if ("MOBILE".equalsIgnoreCase(clientType)) {

                        String json = jsonMapper.writeValueAsString(message);
                        exchange.getMessage().setBody(json);
                        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");

                    } else if ("WEB".equalsIgnoreCase(clientType)) {

                        String xml = xmlMapper.writeValueAsString(message);
                        exchange.getMessage().setBody(xml);
                        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/xml");

                    } else if ("PARTNER".equalsIgnoreCase(clientType)) {

                        String json = jsonMapper.writeValueAsString(message);
                        exchange.getMessage().setBody(json);
                        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");

                    } else {
                        throw new RuntimeException("Unsupported client type");
                    }
                });

    }
}