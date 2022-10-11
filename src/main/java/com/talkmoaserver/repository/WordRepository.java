package com.talkmoaserver.repository;

import com.talkmoaserver.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long>, BatchRepository {
    @Modifying(clearAutomatically = true)
    @Query("update Word w set w.frequency = w.frequency + 1 where w in (:words)")
    int plusCount(List<Word> words);
}


