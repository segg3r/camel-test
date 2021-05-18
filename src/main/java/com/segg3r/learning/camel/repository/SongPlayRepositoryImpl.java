package com.segg3r.learning.camel.repository;

import com.segg3r.learning.camel.model.UserSongPlay;
import com.segg3r.learning.camel.model.UserSongPlays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class SongPlayRepositoryImpl implements SongPlayRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public SongPlayRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserSongPlays getUserSongPlays(long userId) {
        List<Object[]> resultSet = entityManager.createQuery("select u.email, sp.songId, sp.reviewText from User u left join SongPlay sp on u.id = sp.userId where u.id = :id", Object[].class)
                .setParameter("id", userId)
                .getResultList();

        if (resultSet.isEmpty()) {
            throw new IllegalArgumentException("User " + userId + " does not exist.");
        }

        UserSongPlays userSongPlays = new UserSongPlays();
        userSongPlays.setEmail((String) resultSet.get(0)[0]);
        resultSet.forEach(resultRow -> {
            if (resultRow[1] != null) {
                UserSongPlay userSongPlay = new UserSongPlay((long) resultRow[1], (String) resultRow[2]);
                userSongPlays.getSongPlays().add(userSongPlay);
            }
        });

        return userSongPlays;
    }

}
