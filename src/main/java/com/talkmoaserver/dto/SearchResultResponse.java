package com.talkmoaserver.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchResultResponse(
        String keyword,
        int count,
        List<FrequencyResult> usedTalkers
) {
}
