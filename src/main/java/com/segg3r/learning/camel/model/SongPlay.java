package com.segg3r.learning.camel.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class SongPlay implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "song_play_seq_generator")
    @SequenceGenerator(sequenceName = "song_play_seq", allocationSize = 1, name = "song_play_seq_generator")
    private long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "song_id")
    private long songId;
    @Column(name = "duration_ms")
    private long durationMs;
    @Column(name = "review_text")
    private String reviewText;
}
