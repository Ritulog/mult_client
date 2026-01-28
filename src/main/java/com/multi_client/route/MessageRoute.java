package com.multi_client.route;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.multi_client.model.Message;
import com.multi_client.repository.MessageRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageRoute extends RouteBuilder {

    private final MessageRepository repository;

    public MessageRoute(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void configure() {

        onException(RuntimeException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setBody(constant("Message not found"));

        //post
        from("direct:createMessage")
                .log("createMessage route HIT with body: ${body}")
                .process(exchange -> {
                    Message message = exchange.getIn().getBody(Message.class);
                    repository.save(message);
                    exchange.getMessage().setBody(message);
                });




        // get
        from("direct:getMessage")
            .process(exchange -> {

                String id = exchange.getIn().getBody(String.class);
                String clientType = exchange.getIn().getHeader("clientType", String.class);

                Message message = repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Message not found"));

                ObjectMapper jsonMapper = new ObjectMapper();
                XmlMapper xmlMapper = new XmlMapper();

                //  No Strategy / Factory
                //  Simple if-else

                if ("MOBILE".equalsIgnoreCase(clientType)) {
                    String json = jsonMapper.writeValueAsString(message);
                    exchange.getMessage().setBody(json);
                    exchange.getMessage().setHeader("Content-Type", "application/json");

                } else if ("WEB".equalsIgnoreCase(clientType)) {
                    String xml = xmlMapper.writeValueAsString(message);
                    exchange.getMessage().setBody(xml);
                    exchange.getMessage().setHeader("Content-Type", "application/xml");

                } else if ("PARTNER".equalsIgnoreCase(clientType)) {
                    // partner default JSON
                    String json = jsonMapper.writeValueAsString(message);
                    exchange.getMessage().setBody(json);
                    exchange.getMessage().setHeader("Content-Type", "application/json");

                } else {
                    throw new RuntimeException("Unsupported client type");
                }
            });
    }
}
