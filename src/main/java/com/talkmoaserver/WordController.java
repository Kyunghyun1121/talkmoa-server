package com.talkmoaserver;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * REST API 서버의 컨트롤러 단으로 클라이언트에는 JSON 데이터를 내려줄 것이다.
 */
@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @PostMapping("/upload")
    public ResultResponse upload(@RequestParam("file") MultipartFile[] files) {
        Arrays.stream(files).forEach(file -> System.out.println(file.getOriginalFilename()));
        //ResultResponse resultDto = wordService.analyze();
        return new ResultResponse();
    }
}
