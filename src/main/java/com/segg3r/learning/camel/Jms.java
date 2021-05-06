package com.segg3r.learning.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Jms extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:messages")
                .routeId("jms-processor")
                .log("Received a message - ${body} - sending to outbound queue")
                .to("jms:queue:processedMessages?exchangePattern=InOnly");
    }

}
