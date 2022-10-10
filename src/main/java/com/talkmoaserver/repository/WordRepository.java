package com.talkmoaserver.repository;

import com.talkmoaserver.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long>, BatchRepository {
}


