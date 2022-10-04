package com.talkmoaserver.entity;

import lombok.Getter;

import javax.persistence.*;

/**
 * 데이터베이스 테이블 - 객체를 매핑한 Java 객체이다.
 * JPA ORM 기술을 사용하기 위해서 필수로 정의해야하는 객체를 엔티티라고 한다.
 * 이 엔티티 객체를 통해서 디비에서 가져온 데이터를 Java 객체로 매핑시키고,
 * Java 객체에 데이터를 넣어서 디비에 바로 저장을 한다.
 */
@Entity
@Getter
public class Word {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;
    private String word;
    private int frequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public Word(String word) {
        this.word = word;
        this.frequency = 0;
    }

    public void plusCount() {
        this.frequency++;
    }
}
