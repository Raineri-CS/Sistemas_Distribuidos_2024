package com.sisdist.server;

import java.util.Map;

public record OutMessage (
        String operation,
        String status,
        Map<String, String> data
) {}
