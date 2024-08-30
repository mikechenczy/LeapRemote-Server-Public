package com.mj.leapremote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SignIn {

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("username")
    private String username;

    public SignIn(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
