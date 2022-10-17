package com.talkmoaserver.service;

import com.talkmoaserver.dto.FrequencyResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnalyzeWordService {
    private Map<String, List<String>> talkerToToken = new HashMap<>();


    public void save(Map<String, List<String>> talkerToToken) {
        this.talkerToToken.putAll(talkerToToken);
    }

    public List<FrequencyResult> searchWhoUsed(String keyword) {
        List<FrequencyResult> result = new ArrayList<>();

        for (String talker : talkerToToken.keySet()) {
            int usedCount = 0;
            result.add(new FrequencyResult(talker, countWord(talkerToToken.get(talker), keyword)));
        }

        return result;
    }

    public int searchHowManyUsed(String keyword) {
        int wordCount = 0;
        for (String talkList : talkerToToken.keySet()) {
            wordCount += countWord(talkerToToken.get(talkList), keyword);
        }
        return wordCount;
    }

    private int countWord(List<String> talkingList, String keyword) {
        int result = 0;
        for (String talk : talkingList) {
            if (talk.equals(keyword)) result++;
        }
        return result;
    }
}
