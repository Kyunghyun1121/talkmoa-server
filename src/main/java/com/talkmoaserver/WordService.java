package com.talkmoaserver;

import com.talkmoaserver.repository.ChatRoomRepository;
import com.talkmoaserver.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 단어 분석 등 핵심적인 비즈니스 로직을 담당하는 레이어
 */
@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 1. txt파일을 읽어서 대화내역을 받으면 ChatRoom 객체로 만들어서 저장하는 함수

    // 2. 받은 대화 내역을 일단 배열에 저장해서 돌려주는 함수

    // 3. 그 배열을 돌면서 해시맵에 단어 : 빈도수로 매핑시켜서 돌려주는 함수
}
