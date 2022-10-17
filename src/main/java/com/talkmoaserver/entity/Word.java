package com.talkmoaserver.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;
@Deprecated
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Word {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;
    private String keyword;
    private int frequency;
    public Word(String word, int frequency) {
        this.keyword = word;
        this.frequency = frequency;
    }

    public void plusCount() {
        this.frequency++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Word)
            return Objects.equals(this.id, ((Word) obj).id);
        return false;
    }
}
