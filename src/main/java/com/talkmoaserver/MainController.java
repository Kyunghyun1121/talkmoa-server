package com.talkmoaserver;

import com.talkmoaserver.dto.FrequencyResult;
import com.talkmoaserver.dto.SearchResultResponse;
import com.talkmoaserver.service.AnalyzeService;
import com.talkmoaserver.service.ExtractService;
import com.talkmoaserver.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    private final AnalyzeService analyzingService;
    private final ExtractService extractService;
    private final SearchService searchService;
    private final InMemoryStore inMemoryStore;

    @GetMapping
    public String mainPage() {
        return "main";
    }

    /**
     * 대화내역 txt 파일을 업로드 -> 대화 내용 분석
     */
    @PostMapping("/analyze")
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        inMemoryStore.clear();

        // 대화자 : 토큰, 라인 으로 매핑
        extractService.saveFile(file);
        Map<String, List<String>> talkerToToken = extractService.getTalkerToToken();
        Map<String, List<String>> talkerToLine = extractService.getTalkerToLine();

        // 검색을 위해 search 저장
        searchService.save(talkerToToken);

        // 분석 진행
        List<FrequencyResult> total = analyzingService.calcTotal(talkerToToken);
        List<FrequencyResult> ranking = analyzingService.calcManyUseWord(talkerToToken);
        List<FrequencyResult> media = analyzingService.calcMedia(talkerToToken);
        List<FrequencyResult> emoji = analyzingService.calcEmoji(talkerToToken);
        List<FrequencyResult> low = analyzingService.calcTime(talkerToLine, "low");
        List<FrequencyResult> high = analyzingService.calcTime(talkerToLine, "high");

        inMemoryStore.saveLow(low);
        inMemoryStore.saveHigh(high);

        model.addAttribute("roomName", extractService.getRoomName());
        model.addAttribute("total", total);
        model.addAttribute("ranking", ranking);
        model.addAttribute("media", media);
        model.addAttribute("emoji", emoji);
        model.addAttribute("low", low);
        model.addAttribute("high", high);

        return "result";
    }

    @GetMapping("/search")
    public SearchResultResponse search(@RequestParam("keyword") String keyword) {
        List<FrequencyResult> searchWhoUse = searchService.searchWhoUsed(keyword); // 누가 말했는지
        int searchUseNum = searchService.searchHowManyUsed(keyword); // 얼마나 말했는지
        return SearchResultResponse.builder()
                .keyword(keyword)
                .count(searchUseNum)
                .usedTalkers(searchWhoUse)
                .build();
    }

    @GetMapping("/graph/{type}")
    public String graph(@PathVariable String type, Model model) throws IOException {
        model.addAttribute("roomName", extractService.getRoomName());
        model.addAttribute("talkerCount", extractService.getTalkers().size());
        List<FrequencyResult> graph = null;
        String subject = null;
        switch (type) {
            case "low" -> { graph = inMemoryStore.getLow(); subject = "가장 대화를 적게 나눈 시간대"; }
            case "high" -> { graph = inMemoryStore.getHigh();  subject = "가장 대화를 활발하게 나눈 시간대"; }
        }
        model.addAttribute("graph", graph);
        model.addAttribute("subject", subject);
        return "result-graph";
    }
}
