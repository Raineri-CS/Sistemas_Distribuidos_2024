package com.sisdist.common.messages;

import java.util.Map;

public record MESSAGE_TWO_PARAMETERS(
        String operation,
        Map<String, String> data
) implements Message{
    public String getOperation(){ return operation; }
    public Map<String, String> getData(){ return data;}
}
