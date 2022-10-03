package com.talkmoaserver.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String roomName;
    private int totalWordCount;



    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Word> wordList = new ArrayList<>();

    public ChatRoom(long l, String roomname, List wordList) {
        this.id=l;
        this.roomName=roomname;
        this.wordList=wordList;
        totalWordCount = wordList.size();
    }


    public void setId(Long id) {
        this.id = id;
    }



    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }



    public void setTotalWordCount(int totalWordCount) {
        this.totalWordCount = totalWordCount;
    }



    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }





}
