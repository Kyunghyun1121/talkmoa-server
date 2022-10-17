package com.talkmoaserver.repository;

import com.talkmoaserver.entity.Word;

import java.util.List;

@Deprecated
public interface BatchRepository {
    void batchInsert(List<Word> words);
}
