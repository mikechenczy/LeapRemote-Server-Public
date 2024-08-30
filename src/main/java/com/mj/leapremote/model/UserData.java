package com.mj.leapremote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserData {
    @JsonProperty("deviceId")
    public String deviceId;
    @JsonProperty("userId")
    public int userId;
    @JsonProperty("connectId")
    public String connectId;
    @JsonProperty("connectPin")
    public String connectPin;
    @JsonProperty("controlId")
    public int controlId;
    @JsonProperty("devices")
    public String devices;
    @JsonProperty("lastLogin")
    public long lastLogin;
    @JsonProperty("version")
    public String version;
    @JsonProperty("ip")
    public String ip;
    @JsonProperty("deviceInfo")
    public String deviceInfo;
    @JsonProperty("ips")
    public String ips;
}
