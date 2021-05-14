package com.segg3r.learning.camel

import com.segg3r.learning.camel.model.SongPlay
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SongPlayRestSpec extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate
    @Autowired
    private SongPlayRepository songPlayRepository
    @Autowired
    private Flyway flyway

    def cleanup() {
        flyway.clean()
        flyway.migrate()
    }

    def "when song play event is posted it should then be listed"() {
        given: "payload with arbitrary fields"
        def payload = new SongPlay()
        payload.songId = 1L
        payload.userId = 2L
        payload.durationMs = 3000L

        when: "song play event is sent to API"
        testRestTemplate.postForEntity("/api/song_play", payload, SongPlay.class)

        then: "getting list of events should contain a sent event"
        def responseEntity = testRestTemplate.getForEntity("/api/song_play", SongPlay[].class)
        def songPlays = responseEntity.getBody()

        expect: "Single song play event should be present"
        with(songPlays) {
            it != null
            it.length == 1
        }

        and: "Song play event fields should be correctly populated when sent to API"
        with(songPlays[0]) {
            songId == 1L
            userId == 2L
            durationMs == 3000L
            id > 0
            reviewText != null
        }
    }

    def "should return 400 when wrong song play is provided"() {
        given: "payload with incorrect fields"
        def payload = new SongPlay()
        payload.setSongId(-100)
        payload.setUserId(2L)
        payload.setDurationMs(3000L)

        when: "wrong song play event is sent to API"
        def response = testRestTemplate.postForEntity("/api/song_play", payload, String.class)

        then: "response code should be 400"
        response.statusCode.value() == 400
    }

}
