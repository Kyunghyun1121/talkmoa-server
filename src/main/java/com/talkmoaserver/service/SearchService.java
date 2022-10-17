package com.talkmoaserver.service;

import com.talkmoaserver.dto.FrequencyResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    private final Map<String, List<String>> talkerToToken = new HashMap<>();

    public void save(Map<String, List<String>> talkerToToken) {
        this.talkerToToken.putAll(talkerToToken);
    }

    public List<FrequencyResult> searchWhoUsed(String keyword) {
        List<FrequencyResult> result = new ArrayList<>();
        for (String talker : talkerToToken.keySet()) {
            int count = countWord(talkerToToken.get(talker), keyword);
            if (count == 0) continue;
            result.add(new FrequencyResult(talker, count));
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

    public void clear() {
        this.talkerToToken.clear();
    }
}
