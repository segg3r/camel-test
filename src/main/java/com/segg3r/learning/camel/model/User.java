package com.segg3r.learning.camel.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "[user]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    @SequenceGenerator(sequenceName = "[user_seq]", allocationSize = 1, name = "user_seq_generator")
    private long id;
    @Column(name = "email")
    private String email;
}
