package com.talkmoaserver.dto;

public record FrequencyResult(
        String subject, // 주체 : talker, period, word 등 될 수 있음
        int frequency
) { }
