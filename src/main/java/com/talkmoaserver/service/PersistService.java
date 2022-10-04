package com.talkmoaserver.service;

import com.talkmoaserver.entity.ChatRoom;
import com.talkmoaserver.entity.Word;
import com.talkmoaserver.repository.ChatRoomRepository;
import com.talkmoaserver.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Word, ChatRoom 을 엔티티화 하여 DB에 영속화 하는 서비스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PersistService {
    private final WordRepository wordRepository;
    private final ChatRoomRepository chatRoomRepository;

    // TODO : Insert 성능이 안좋음 -> Batch 옵션으로 최적화 할 것
    public void save(String roomName, List<String> totalTokenizedWords) {
        List<Word> savedWords = new ArrayList<>();
        for (String token : totalTokenizedWords) {
            Optional<Word> wordEntity = wordRepository.findByWord(token);
            if (wordEntity.isPresent()) {
                wordEntity.get().plusCount();
            } else {
                savedWords.add(new Word(token));
            }
        }
        chatRoomRepository.save(new ChatRoom(roomName, savedWords));
    }

    // TODO : 전체 사용자 단어 랭킹 조회 메서드 구현 해야함
//    public ResultResponse getTotalWordRank() {
//        return new ResultResponse(null, null);
//    }
}
