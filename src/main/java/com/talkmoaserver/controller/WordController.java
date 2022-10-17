package com.talkmoaserver.controller;

import com.talkmoaserver.dto.FrequencyResult;
import com.talkmoaserver.dto.ResultResponse;
import com.talkmoaserver.dto.SearchResultResponse;
import com.talkmoaserver.service.AnalyzeService;
import com.talkmoaserver.service.ExtractService;
import com.talkmoaserver.service.PersistService;
import com.talkmoaserver.service.AnalyzeWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WordController {
    private final AnalyzeService analyzingService;
    private final ExtractService extractService;
    private final PersistService persistService;
    private final AnalyzeWordService analyzeWordservice;

    /**
     * 대화내역 txt 파일을 업로드 -> 대화 내용 분석
     */
    @PostMapping("/analyze")
    public ResultResponse upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        // 일단 파일들로 받지만, 하나의 파일만 처리하도록 구현한다

        // 대화자 : 토큰, 라인 으로 매핑
        extractService.saveFile(files[0]);
        Map<String, List<String>> talkerToToken = extractService.getTalkerToToken();
        Map<String, List<String>> talkerToLine = extractService.getTalkerToLine();

        // 단어를 wordstore에 저장
        analyzeWordservice.save(talkerToToken);

        // 분석 진행
        List<FrequencyResult> total = analyzingService.calcTotal(talkerToToken);
        List<FrequencyResult> ranking = analyzingService.calcManyUseWord(talkerToToken);
        List<FrequencyResult> media = analyzingService.calcMedia(talkerToToken);
        List<FrequencyResult> emoji = analyzingService.calcEmoji(talkerToToken);
        List<FrequencyResult> low = analyzingService.calcTime(talkerToLine, "low");
        List<FrequencyResult> high = analyzingService.calcTime(talkerToLine, "high");


        // 분석 결과를 DTO 로 응답
        return ResultResponse.builder()
                .chatRoomName(extractService.getRoomName())
                .talkers(extractService.getTalkers())
                .total(total)
                .wordRanking(ranking)
                .media(media)
                .emoji(emoji)
                .lowPeriod(low)
                .highPeriod(high)
                .build();
    }

    @GetMapping("/search/{keyword}")
    public SearchResultResponse search(@PathVariable String keyword) {


        keyword = keyword.substring(1, keyword.length() - 1);

        List<FrequencyResult> searchWhoUse = analyzeWordservice.searchWhoUsed(keyword); // 누가 말했는지
        int searchUseNum = analyzeWordservice.searchHowManyUsed(keyword); // 얼마나 말했는지

        // TODO
        return SearchResultResponse.builder()
                .useNumber(searchUseNum)
                .usedTalker(searchWhoUse)
                .build();
    }
}
