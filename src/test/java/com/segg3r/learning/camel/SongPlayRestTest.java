package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.assertj.core.util.Lists;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class SongPlayRestTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private SongPlayRepository songPlayRepository;
	@Autowired
	private Flyway flyway;

	@After
	public void resetDatabase() {
		flyway.clean();
		flyway.migrate();
	}

	@Test
	public void testJmsMessageIsPopulatedWhenRestIsCalled() {
		SongPlay payload = new SongPlay();
		payload.setSongId(1L);
		payload.setUserId(2L);
		payload.setDurationMs(3000L);

		testRestTemplate.postForEntity("/api/song_play", payload, SongPlay.class);

		List<SongPlay> songPlays = Lists.newArrayList(songPlayRepository.findAll());
		assertEquals(1, songPlays.size());

		SongPlay songPlay = songPlays.get(0);
		assertEquals(1L, songPlay.getSongId());
		assertEquals(2L, songPlay.getUserId());
		assertEquals(3000L, songPlay.getDurationMs());
		assertTrue(songPlay.getId() > 0);
		assertNotNull(songPlay.getReviewText());
	}

	@Test
	public void testListSongPlaysRest() {
		SongPlay payload = new SongPlay();
		payload.setSongId(1L);
		payload.setUserId(2L);
		payload.setDurationMs(3000L);
		testRestTemplate.postForEntity("/api/song_play", payload, SongPlay.class);

		ResponseEntity<SongPlay[]> responseEntity = testRestTemplate.getForEntity("/api/song_play", SongPlay[].class);
		SongPlay[] songPlays = responseEntity.getBody();

		assertNotNull(songPlays);
		assertEquals(1, songPlays.length);

		SongPlay songPlay = songPlays[0];
		assertEquals(1L, songPlay.getSongId());
		assertEquals(2L, songPlay.getUserId());
		assertEquals(3000L, songPlay.getDurationMs());
		assertTrue(songPlay.getId() > 0);
		assertNotNull(songPlay.getReviewText());
	}

}
