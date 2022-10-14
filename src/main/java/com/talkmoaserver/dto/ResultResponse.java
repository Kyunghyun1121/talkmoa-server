package com.talkmoaserver.dto;

import lombok.Builder;

import java.util.List;

/**
 * 대화 내용 분석 결과를 이 객체에 담아서 프론트로 내려줄 것 임.
 */
@Builder
public record ResultResponse(
        String chatRoomName,
        List<String> talkers,
        List<FrequencyResult> total,
        List<FrequencyResult> wordRanking,
        List<FrequencyResult> media,
        List<FrequencyResult> emoji,
        List<FrequencyResult> lowPeriod,
        List<FrequencyResult> highPeriod
) { }
