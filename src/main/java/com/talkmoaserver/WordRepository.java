package com.talkmoaserver;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MySQL 데이터베이스와 연동을 관장할 객체
 * WordRepository 를 의존성 주입받아서 서비스 레이어에서 사용하면 된다.
 * save() = 엔티티 저장 (디비 INSERT)
 * findByXX() = 엔티티 조회 (디비 SELECT)
 * delete() = 엔티티 삭제 (디비 DELETE)
 * UPDATE 쿼리는 Dirty Checking을 사용할 예정
 * 인터페이스이지만, JPA가 알맞은 구현체를 주입해준다.
 */
public interface WordRepository extends JpaRepository<Word, Long> { }
