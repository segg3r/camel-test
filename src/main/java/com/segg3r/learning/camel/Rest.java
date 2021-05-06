package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.InputMessage;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Rest extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .contextPath("/api")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel REST API")
                .apiProperty("api.version", "1.0")
                .component("servlet");

        rest("/message")
                .consumes("application/json")
                .bindingMode(RestBindingMode.json)
                .post()
                        .type(InputMessage.class)
                        .responseMessage()
                                .code(200)
                                .message("If message is successfully sent")
                                .endResponseMessage()
                        .route()
                                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("200"))
                                .transform()
                                .simple("${body.text}")
                                .log("Received message ${body}")
                                .to("jms:queue:messages")
        .endRest();
    }

}
