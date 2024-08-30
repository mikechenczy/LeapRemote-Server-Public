package com.mj.leapremote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("createTime")
    private long createTime;

    @JsonProperty("vip")
    private long vip;

    @JsonProperty("vipType")
    private int vipType;

    @JsonProperty("money")
    private int money;

    @JsonProperty("finalLogin")
    private long finalLogin;

    @JsonProperty("device")
    private String device;

    @JsonProperty("ip")
    private String ip;
}
