package com.mj.leapremote.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Device {
    public static final int MODE_MANUAL = 0;
    public static final int MODE_DIRECT = 1;
    public static final int OS_UNKNOWN = -1;
    public static final int OS_ANDROID = 0;
    public static final int OS_WINDOWS = 1;
    public static final int STATUS_OFFLINE = 0;
    public static final int STATUS_ONLINE = 1;
    public static final int STATUS_NOT_ENABLED = 2;
    public static final int STATUS_DIRECT = 5;

    private int userId;
    private String connectId;
    private String connectPin;
    private String name;
    private int status;
    private int mode;
    private int operateSystem;
}
