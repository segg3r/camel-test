package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SongPlayGenerator extends RouteBuilder {

    @Override
    public void configure() {
        from("quartz2://camelTest/songPlayGenerator?trigger.repeatInterval=10000&stateful=true")
                .routeId("song-play-generator")
                .transform()
                .body(() -> {
                    Random random = new Random(System.currentTimeMillis());

                    SongPlay songPlay = new SongPlay();
                    songPlay.setSongId(random.nextLong());
                    songPlay.setUserId(random.nextLong());
                    songPlay.setDurationMs(random.nextLong());

                    log.info("Generating song play: " + songPlay);
                    return songPlay;
                })
                .to("jms:queue:song_plays");
    }

}
