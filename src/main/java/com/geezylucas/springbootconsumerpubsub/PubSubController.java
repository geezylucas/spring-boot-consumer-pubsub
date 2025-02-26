package com.geezylucas.springbootconsumerpubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geezylucas.springbootconsumerpubsub.model.Body;
import com.geezylucas.springbootconsumerpubsub.model.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.Base64;

// PubsubController consumes a Pub/Sub message.
@Slf4j
@RestController
@RequiredArgsConstructor
public class PubSubController {

    private final ObjectMapper objectMapper;

    @PostMapping("/")
    public Mono<ResponseEntity<String>> receiveMessage(@RequestBody Body body) {
        // Get PubSub message from request body.
        Body.Message message = body.getMessage();
        if (message == null) {
            String msg = "Bad Request: invalid Pub/Sub message format";
            log.error(msg);
            return Mono.just(new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST));
        }

        String data = message.getData();
        UserMessage userMessage;
        try {
            userMessage = objectMapper.readValue(new String(Base64.getDecoder().decode(data)), UserMessage.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }

        SecureRandom rand = new SecureRandom();
        if (rand.nextInt(1, 3) == userMessage.getRandom()) {
            log.info("Random number is {}, retry", userMessage.getRandom());
            return Mono.just(new ResponseEntity<>("User invalid", HttpStatus.INTERNAL_SERVER_ERROR));
        }

        log.info("UserMessage info: {}", userMessage);
        return Mono.just(new ResponseEntity<>(userMessage.getBody(), HttpStatus.OK));
    }
}
// [END cloudrun_pubsub_handler]