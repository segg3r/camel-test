package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongPlayProcessor extends RouteBuilder {

    private final SongPlayRepository songPlayRepository;

    @Autowired
    public SongPlayProcessor(SongPlayRepository songPlayRepository) {
        this.songPlayRepository = songPlayRepository;
    }

    @Override
    public void configure() {
        from("jms:queue:song_plays")
                .routeId("song-play-processor")
                .log("Received a song play from a queue: ${body.toString()}, saving to a database.")
                .process(exchange -> {
                    SongPlay songPlay = exchange.getMessage(JmsMessage.class).getBody(SongPlay.class);
                    log.info("Extracted song play from jms queue: " + songPlay.toString());
                    songPlayRepository.save(songPlay);
                });
    }

}
