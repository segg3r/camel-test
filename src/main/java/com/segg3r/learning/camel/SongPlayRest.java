package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongPlayRest extends RouteBuilder {

    private final SongPlayRepository songPlayRepository;

    @Autowired
    public SongPlayRest(SongPlayRepository songPlayRepository) {
        this.songPlayRepository = songPlayRepository;
    }

    @Override
    public void configure() {
        restConfiguration()
                .contextPath("/api")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel REST API")
                .apiProperty("api.version", "1.0")
                .component("servlet");

        rest("/song_play")
                .consumes("application/json")
                .produces("application/json")
                .bindingMode(RestBindingMode.json)
                .post()
                        .type(SongPlay.class)
                        .responseMessage()
                                .code(200)
                                .message("If message is successfully sent")
                                .endResponseMessage()
                        .route()
                                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("204"))
                                .log("REST sent a new song play: ${body.toString()}")
                                .wireTap("jms:queue:song_plays?messageConverter=#jmsJsonMessageConverter")
                                .transform(constant(null))
                        .endRest()
                .get()
                        .responseMessage()
                                .code(200)
                                .message("If could return a list of messages")
                                .endResponseMessage()
                        .route()
                                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("200"))
                                .transform()
                                .body(songPlayRepository::findAll)
                        .endRest();
    }

}
