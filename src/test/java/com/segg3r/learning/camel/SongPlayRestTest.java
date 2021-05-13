package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class SongPlayRestTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@EndpointInject("mock:jms:queue:song_plays")
	private MockEndpoint jmsQueueMock;

	@Test
	public void testJmsMessageIsPopulatedWhenRestIsCalled() {
		SongPlay payload = new SongPlay();
		payload.setSongId(1L);
		payload.setUserId(2L);
		payload.setDurationMs(3000L);

		testRestTemplate.postForEntity("/api/song_play", payload, SongPlay.class);
		jmsQueueMock.expectedBodyReceived();
	}

}
