package com.segg3r.learning.camel.repository;

import com.segg3r.learning.camel.model.UserSongPlays;

public interface SongPlayRepositoryCustom {
    UserSongPlays getUserSongPlays(long userId);
}
