package com.segg3r.learning.camel;

import com.segg3r.learning.camel.model.SongPlay;
import org.springframework.data.repository.CrudRepository;

public interface SongPlayRepository extends CrudRepository<SongPlay, Long> {
}
