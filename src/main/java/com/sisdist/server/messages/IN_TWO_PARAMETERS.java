package com.sisdist.server.messages;

import java.util.Map;

public record IN_TWO_PARAMETERS(
        String operation,
        Map<String, String> data
) implements Message{
    public String getOperation(){ return operation; }
}
