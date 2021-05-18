package com.segg3r.learning.camel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSongPlay {
    private long songId;
    private String reviewText;
}
