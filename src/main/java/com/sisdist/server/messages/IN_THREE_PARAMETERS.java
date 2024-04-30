package com.sisdist.server.messages;

import java.util.Map;

public record IN_THREE_PARAMETERS(
        // FIXME checar se token vem como string
        String operation,
        String token,
        Map<String, String> data

) implements Message{
    public String getOperation(){ return operation; }
}
