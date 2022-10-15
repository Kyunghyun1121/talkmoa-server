package com.talkmoaserver.controller;

import com.talkmoaserver.dto.FrequencyResult;
import com.talkmoaserver.dto.ResultResponse;
import com.talkmoaserver.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final AnalyzeService analyzeService;

    @GetMapping("/test")
    public List<FrequencyResult> test() {
        Map<String, List<String>> temp = new HashMap<>();
        List<String> tempWords = new ArrayList<>();
        tempWords.add("바보");
        tempWords.add("사진");
        tempWords.add("부리또는 맛있다.");
        tempWords.add("오전 12:21");tempWords.add("오전 10:21");tempWords.add("오전 02:21");
        tempWords.add("오전 11:21");tempWords.add("오전 12:21");tempWords.add("오전 12:21");
        tempWords.add("오후 12:21");tempWords.add("오후 11:21");tempWords.add("오전 02:21");
        temp.put("노경현", tempWords);
        // 결과 = 노경현 : 1 media라면

        log.info("노경현 결과물 = {}", temp); // 실무용
        System.out.println("노경현 결과물 = " + temp); // 이건 실무에서 쓰면 암살당함

        List<FrequencyResult> frequencyResults = analyzeService.calcTime(temp, "low");
        List<FrequencyResult> TfrequencyResults = analyzeService.calcTime(temp, "high");
//        List<FrequencyResult> result = new ArrayList<>();
//        result.add(new FrequencyResult("노경현", 123123123));
        return TfrequencyResults;
    }

    @PostMapping("/test/upload")
    public ResultResponse testForFront(@RequestParam("file") MultipartFile file) {
        List<FrequencyResult> total = new ArrayList<>();
        total.add(new FrequencyResult("이준형", 3333));
        total.add(new FrequencyResult("김원철", 123));

        List<FrequencyResult> media = new ArrayList<>();
        media.add(new FrequencyResult("이준형", 23));
        media.add(new FrequencyResult("김원철", 1));

        List<FrequencyResult> emoji = new ArrayList<>();
        emoji.add(new FrequencyResult("이준형", 100));
        emoji.add(new FrequencyResult("김원철", 10));

        List<FrequencyResult> low = new ArrayList<>();
        low.add(new FrequencyResult("10시~11시", 32));
        low.add(new FrequencyResult("11시~12시", 10));

        List<FrequencyResult> high = new ArrayList<>();
        high.add(new FrequencyResult("15시~16시", 1));
        high.add(new FrequencyResult("17시~18시", 2));

        List<String> talkers = new ArrayList<>();
        talkers.add("김원철");
        talkers.add("이준형");

        return ResultResponse.builder()
                .chatRoomName("팀프1")
                .talkers(talkers)
                .total(total)
                .media(media)
                .emoji(emoji)
                .lowPeriod(low)
                .highPeriod(high)
                .build();
    }
}
