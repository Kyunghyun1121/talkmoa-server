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
        temp.put("노경현", tempWords);
        // 결과 = 노경현 : 1 media라면

        log.info("노경현 결과물 = {}", temp); // 실무용
        System.out.println("노경현 결과물 = " + temp); // 이건 실무에서 쓰면 암살당함

        List<FrequencyResult> frequencyResults = analyzeService.calcMedia(temp);
//        List<FrequencyResult> result = new ArrayList<>();
//        result.add(new FrequencyResult("노경현", 123123123));
        return frequencyResults;
    }

    @PostMapping("/test/upload")
    public ResultResponse testForFront(@RequestParam("file") MultipartFile file) {
        List<FrequencyResult> total = new ArrayList<>();
        total.add(new FrequencyResult("이준형", 3333));
        total.add(new FrequencyResult("김원철", 123));

        List<FrequencyResult> media = new ArrayList<>();
        media.add(new FrequencyResult("이준형", 11));
        media.add(new FrequencyResult("김원철", 23));

        List<FrequencyResult> emoji = new ArrayList<>();
        emoji.add(new FrequencyResult("이준형", 0));
        emoji.add(new FrequencyResult("김원철", 100));

        List<FrequencyResult> low = new ArrayList<>();
        low.add(new FrequencyResult("10시~11시", 32));
        low.add(new FrequencyResult("11시~12시", 10));

        List<FrequencyResult> high = new ArrayList<>();
        high.add(new FrequencyResult("15시~16시", 1));
        high.add(new FrequencyResult("17시~18시", 2));

        return ResultResponse.builder()
                .total(total)
                .media(media)
                .emoji(emoji)
                .lowPeriod(low)
                .highPeriod(high)
                .build();
    }
}
