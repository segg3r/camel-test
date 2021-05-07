package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class SongPlayRest extends RouteBuilder {

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
                .bindingMode(RestBindingMode.json)
                .post()
                        .type(SongPlay.class)
                        .responseMessage()
                                .code(200)
                                .message("If message is successfully sent")
                                .endResponseMessage()
                        .route()
                                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("200"))
                                .log("REST sent a new song play: ${body.toString()}")
                                .transform()
                                .body(SongPlay.class)
                                .log("Sending to a queue: ${body.toString()}")
                                .to("jms:queue:song_plays")
        .endRest();
    }

}
