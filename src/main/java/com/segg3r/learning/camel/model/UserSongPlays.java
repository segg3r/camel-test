package com.segg3r.learning.camel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSongPlays {
    private String email;
    private List<UserSongPlay> songPlays = new ArrayList<>();
}
