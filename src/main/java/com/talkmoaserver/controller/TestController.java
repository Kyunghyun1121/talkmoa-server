package com.talkmoaserver.controller;

import com.talkmoaserver.dto.FrequencyResult;
import com.talkmoaserver.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
