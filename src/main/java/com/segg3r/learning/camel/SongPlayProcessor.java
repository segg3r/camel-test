package com.segg3r.learning.camel;

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
        from("jms:queue:song_plays")
                .routeId("song-play-processor")
                .log("Received a song play from a queue: ${body.toString()}, saving to a database.")
                .process(exchange -> {
                    SongPlay songPlay = exchange.getMessage(JmsMessage.class).getBody(SongPlay.class);
                    songPlay.setReviewText(
                            songReviewTextProducer.produceSongReview(songPlay.getSongId(), songPlay.getUserId()));
                    log.info("Extracted song play from jms queue: " + songPlay);
                    songPlayRepository.save(songPlay);
                });
    }

}
