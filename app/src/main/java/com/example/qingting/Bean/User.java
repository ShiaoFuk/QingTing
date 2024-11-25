package com.example.qingting.Bean;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
    Integer id;
    String username;
    @Setter
    String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
