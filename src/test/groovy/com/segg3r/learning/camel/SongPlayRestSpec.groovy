package com.segg3r.learning.camel

import com.segg3r.learning.camel.model.SongPlay
import com.segg3r.learning.camel.model.UserSongPlays
import com.segg3r.learning.camel.repository.SongPlayRepositoryCustom
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SongPlayRestSpec extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate
    @Autowired
    private SongPlayRepositoryCustom songPlayRepository
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
        payload.userId = 1L
        payload.durationMs = 3000L

        when: "song play event is sent to API"
        testRestTemplate.postForEntity("/api/song_play", payload, SongPlay.class)

        then: "getting list of user song plays should contain a sent event"
        def responseEntity = testRestTemplate.getForEntity("/api/song_play/user/1", UserSongPlays.class)
        def userSongPlays = responseEntity.getBody()

        expect: "Single song play event should be present"
        with(userSongPlays) {
            it != null
            it.email == 'segg3r@gmail.com'
            it.songPlays.size() == 1
        }

        and: "Song play event fields should be correctly populated when sent to API"
        with(userSongPlays.songPlays[0]) {
            songId == 1L
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
