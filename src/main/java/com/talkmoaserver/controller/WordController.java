package com.talkmoaserver.controller;

import com.talkmoaserver.dto.FrequencyResult;
import com.talkmoaserver.dto.ResultResponse;
import com.talkmoaserver.service.AnalyzeService;
import com.talkmoaserver.service.ParseService;
import com.talkmoaserver.service.PersistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WordController {
    private final AnalyzeService analyzingService;
    private final ParseService parseService;
    private final PersistService wordService;

    /**
     * 대화내역 txt 파일을 업로드 -> 대화 내용 분석
     */
    @PostMapping("/analyze")
    public ResultResponse upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        // 일단 파일들로 받지만, 하나의 파일만 처리하도록 구현한다

        // 대화자 : 토큰 리스트 으로 매핑
        Map<String, List<String>> talkerToWords = parseService.parse(files[0]);

        // 단어를 DB에 저장
//        wordService.save(parseService.getRoomName(), parseService.tokenizeTotal());

        log.info("채팅방 = {}, 대화자들 = {}", parseService.getRoomName(), parseService.getTalkers());

        // 분석 진행
        List<FrequencyResult> total = analyzingService.calcTotal(talkerToWords);
        List<FrequencyResult> media = analyzingService.calcMedia(talkerToWords);
        List<FrequencyResult> emoji = analyzingService.calcEmoji(talkerToWords);
        List<FrequencyResult> low = analyzingService.calcTime(talkerToWords, "low");
        List<FrequencyResult> high = analyzingService.calcTime(talkerToWords, "high");

        // 분석 결과를 DTO 로 응답
        return ResultResponse.builder()
                .total(total)
                .media(media)
                .emoji(emoji)
                .lowPeriod(low)
                .highPeriod(high)
                .build();
    }

    /**
     * TODO : 전체 사용자 단어 랭킹(통계) 조회
     */
    //@GetMapping("/total-rank")
    //public ResultResponse getTotalRank() {
    //    return new ResultResponse();
    //}
    
    
}
