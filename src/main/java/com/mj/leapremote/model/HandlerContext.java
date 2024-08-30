package com.mj.leapremote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HandlerContext {
    @JsonProperty("description")
    private String description;

    @JsonProperty("content")
    private String content;
}
