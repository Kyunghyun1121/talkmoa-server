package com.talkmoaserver.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchResultResponse(
        int useNumber,
        List<FrequencyResult> usedTalker
) {
}
