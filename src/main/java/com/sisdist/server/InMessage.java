package com.sisdist.server;

import java.util.Map;

public record InMessage(
   String operation,
   Map<String, String> data
){}
