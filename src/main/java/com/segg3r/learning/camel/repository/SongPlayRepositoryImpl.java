package com.segg3r.learning.camel.repository;

import com.segg3r.learning.camel.model.SongPlay;
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
        String email = entityManager.createQuery("select u.email from User u where u.id = :id", String.class)
                .setParameter("id", userId)
                .getSingleResult();
        List<SongPlay> songPlays = entityManager.createQuery("select sp from SongPlay sp where sp.userId = :userId", SongPlay.class)
                .setParameter("userId", userId)
                .getResultList();

        return new UserSongPlays(email, songPlays);
    }

}
