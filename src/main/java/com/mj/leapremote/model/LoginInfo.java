package com.mj.leapremote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginInfo {

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("lastTime")
    private String lastTime;

    @JsonProperty("location")
    private String location;

    @JsonProperty("device")
    private String device;

    @JsonProperty("version")
    private String version;
}
