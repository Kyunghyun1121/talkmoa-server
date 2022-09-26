package com.talkmoaserver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 데이터베이스 테이블 - 객체를 매핑한 Java 객체이다.
 * JPA ORM 기술을 사용하기 위해서 필수로 정의해야하는 객체를 엔티티라고 한다.
 * 이 엔티티 객체를 통해서 디비에서 가져온 데이터를 Java 객체로 매핑시키고,
 * Java 객체에 데이터를 넣어서 디비에 바로 저장을 한다.
 */
@Entity
public class Word {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;
    private int frequency;
}
