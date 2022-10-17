package com.talkmoaserver.service;

import com.talkmoaserver.dto.FrequencyResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WordStore {
    private final Map<String, List<String>> talkerToToken = new HashMap<>();

    public FrequencyResult search(String keyword) { return null; }
    public void save(List<String> wordList) {}
}
