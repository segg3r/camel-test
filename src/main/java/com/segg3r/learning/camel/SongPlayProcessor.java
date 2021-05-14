package com.segg3r.learning.camel;

import com.segg3r.learning.camel.exception.BusinessException;
import com.segg3r.learning.camel.model.SongPlay;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SongPlayProcessor extends RouteBuilder {

    private final SongPlayRepository songPlayRepository;
    private final SongReviewTextProducer songReviewTextProducer;

    @Override
    public void configure() {
        onException(BusinessException.class)
                .routeId("business_exception")
                .handled(true)
                .maximumRedeliveries(0)
                .wireTap("direct:exception_jms_dead_letter_queue")
                .end();

        from("direct:exception_jms_dead_letter_queue")
                .filter(exchange -> exchange.getIn() instanceof JmsMessage)
                .routeId("exception_jms_dead_letter_queue")
                .log("Sending a message to a dead letter queue: ${body.toString()}")
                .to("jms:queue:dead?messageConverter=#jmsJsonMessageConverter")
                .end();

        from("jms:queue:song_plays?messageConverter=#jmsJsonMessageConverter")
                .routeId("song-play-processor")
                .process(exchange -> {
                    SongPlay songPlay = exchange.getMessage(JmsMessage.class).getBody(SongPlay.class);
                    songPlay.setReviewText(
                            songReviewTextProducer.produceSongReview(songPlay.getSongId(), songPlay.getUserId()));
                    songPlayRepository.save(songPlay);

                    log.info("Saved new song play: " + songPlay);
                });
    }

}
