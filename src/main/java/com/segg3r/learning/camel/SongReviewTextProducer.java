package com.segg3r.learning.camel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SongReviewTextProducer {

    private static final Logger LOG = LogManager.getLogger(SongReviewTextProducer.class);

    @Cacheable(value = "songReviews", key = "#songId + '.' + #userId")
    public String produceSongReview(long songId, long userId) {
        LOG.info("Producing song review for song " + songId + " by user " + userId);
        return "Song Review";
    }

}
