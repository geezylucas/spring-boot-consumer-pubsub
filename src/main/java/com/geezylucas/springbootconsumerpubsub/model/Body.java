package com.geezylucas.springbootconsumerpubsub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body.Message is the payload of a Pub/Sub event. Please refer to the docs for
// additional information regarding Pub/Sub events.
@Getter
@Setter
@NoArgsConstructor
public class Body {

    private Message message;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {

        private String messageId;
        private String publishTime;
        private String data;
    }
}