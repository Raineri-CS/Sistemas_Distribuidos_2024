package com.sisdist.common.messages;

import java.util.Map;

public record MESSAGE_THREE_PARAMETERS_WITH_TOKEN(
        String operation,
        String token,
        Map<String, String> data

) implements Message{
    public String getOperation(){ return operation; }
}
