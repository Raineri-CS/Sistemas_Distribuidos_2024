package com.sisdist.common.messages;

import java.util.Map;

public record OUT_THREE_PARAMETERS(
        String operation,
        String status,
        Map<String, String> data
) implements Message{
    public String getOperation(){ return operation;}
    public Map<String, String> getData(){ return data;}
}
