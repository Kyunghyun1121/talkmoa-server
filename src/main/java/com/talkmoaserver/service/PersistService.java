package com.talkmoaserver.service;

import com.talkmoaserver.entity.Word;
import com.talkmoaserver.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersistService {
    private final WordRepository wordRepository;

    @Transactional
    public void saveAll(List<String> totalTokenizedWords) {
        List<Word> wordEntities = totalTokenizedWords.stream().map(Word::new).toList();
        wordRepository.batchInsert(wordEntities);
    }

    public Long countAll() {
        return wordRepository.count();
    }

    // TODO : 전체 사용자 단어 랭킹 조회 메서드 구현 해야함
//    public ResultResponse getTotalWordRank() {
//        return new ResultResponse(null, null);
//    }
}
