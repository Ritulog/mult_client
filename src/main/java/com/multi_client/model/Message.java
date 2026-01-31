package com.multi_client.model;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@Setter
public class Message {

    @Id
    private ObjectId id;
    private String sender;
    private String content;

    // getters & setters
}
