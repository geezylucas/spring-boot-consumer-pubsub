package com.geezylucas.springbootconsumerpubsub;

import com.geezylucas.springbootconsumerpubsub.model.Body;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Base64;

// PubsubController consumes a Pub/Sub message.
@Slf4j
@RestController
public class PubSubController {

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
        String target = !StringUtils.isEmpty(data) ? new String(Base64.getDecoder().decode(data)) : "World";
        String msg = "Hello " + target + "!";

        log.info(msg);
        return Mono.just(new ResponseEntity<>(msg, HttpStatus.OK));
    }
}
// [END cloudrun_pubsub_handler]