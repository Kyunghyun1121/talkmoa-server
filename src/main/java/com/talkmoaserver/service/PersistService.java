package com.talkmoaserver.service;

import com.talkmoaserver.entity.Word;
import com.talkmoaserver.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersistService {
    private final WordRepository wordRepository;

    @Transactional
    public void saveAll(Map<String, List<String>> talkerToToken) {
        long start = System.currentTimeMillis();
        int totalWords = 0;
        for (String talker : talkerToToken.keySet()) {
            List<String> tokens = talkerToToken.get(talker);
            totalWords += tokens.size();
            log.info("대화자 = {}, 단어수 = {}", talker, tokens.size());
            List<Word> newEntities = new ArrayList<>();
            List<Word> savedWords = wordRepository.findAll();
            for (String token : tokens) {
                Word newWord = new Word(token, count(tokens, token));
                int index = savedWords.indexOf(newWord);
                if (index == -1) {
                    newEntities.add(new Word(token, count(tokens, token)));
                } else {
                    savedWords.get(index).plusCount();
                }
            }
            long start2 = System.currentTimeMillis();
            wordRepository.batchInsert(newEntities);
            long end2 = System.currentTimeMillis();
            log.info("새로 넣는 총 단어 = {}, batchInsert 수행 시간 시간 = {}s", newEntities.size(), (end2 - start2) / 1000.0);

        }
        long end = System.currentTimeMillis();
        log.info("총 단어 = {}, 수행 시간 = {}s", totalWords, (end - start) / 1000.0);
    }

    public Long countAll() {
        return wordRepository.count();
    }

    public int count(List<String> tokens, String target) {
        List<String> temp = new ArrayList<>(tokens);
        int count = 0;
        for (String token : temp) {
            if (token.startsWith(target)) count++;
        }
        return count;
    }
}
