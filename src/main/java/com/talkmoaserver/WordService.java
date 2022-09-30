package com.talkmoaserver;

import com.talkmoaserver.entity.ChatRoom;
import com.talkmoaserver.entity.Word;
import com.talkmoaserver.repository.ChatRoomRepository;
import com.talkmoaserver.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * 단어 분석 등 핵심적인 비즈니스 로직을 담당하는 레이어
 */
@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final ChatRoomRepository chatRoomRepository;
    private static long sequence = 0L;
    private int tnum = 0;

    // 1. txt파일을 읽어서 대화내역을 받으면 ChatRoom 객체로 만들어서 저장하는 함수
    public ChatRoom storechat(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String roomname = scanner.next();

        // 처음에 날짜가 있어서 넘김
        String empty = scanner.nextLine();
        empty = scanner.nextLine();

        ChatRoom chatroom = new ChatRoom();

        String[] list = new String[]{};
        int coun = 0;

        //파일을 읽으며 문장 배열을 만듬
        while (scanner.hasNext()) {
            String str = scanner.next();
            list[coun++]=str;
        }

        chatroom.setId(sequence++);
        chatroom.setRoomName(roomname);
        chatroom.setWordList(split(list,coun));
        chatroom.setTotalWordCount(chatroom.getWordList().size());
        return chatroom;
    }


    // 2. 받은 대화 내역을 일단 배열에 저장해서 돌려주는 함수

    public List split(String[] line,int num){
        HashMap<String, Integer>talker = new HashMap<>();
        HashMap<String, Integer>timer = new HashMap<>();
        List<String> talk = new ArrayList<>();

        for(int b=0; b<num; b++){
            char[] arr = line[b].toCharArray();
            if(arr[0]=='-')continue;
            if(arr[0]!='['){
                talk.add(line[b]);
                continue;
            }

            //대화자 저장
            int coun = 0;
            char[] name = new char[0];
            for(int a=0; arr[coun]!=']'; a++){
                name[a] = arr[coun++];
            }
            if(!talker.containsKey(String.valueOf(name))){
                talker.put(String.valueOf(name),1);
            }else{
                talker.put(String.valueOf(name), talker.get(String.valueOf(name))+1);
            }

            coun+=2;
            //대화 시간 저장
            char[] time = new char[0];
            for(int a=0; arr[coun]!=']'; a++){
                time[a] = arr[coun++];
            }
            if(!timer.containsKey(String.valueOf(time))){
                timer.put(String.valueOf(time),1);
            }else{
                timer.put(String.valueOf(time), timer.get(String.valueOf(time))+1);
            }

            //대화 내역 저장
            char[] talking = new char[0];
            for(int a=0; arr[coun]!=0; a++){
                talking[a] = arr[coun++];
            }
            talk.add(String.valueOf(talking));

        }

        return talk;
    }

    // 3. 그 배열을 돌면서 해시맵에 단어 : 빈도수로 매핑시켜서 돌려주는 함수
    public HashMap analyze(ArrayList<?> wordlist){
        HashMap<String, Integer>talk = new HashMap<>();

        while (!wordlist.isEmpty()){
            String str = (String) wordlist.remove(0);

            if(!talk.containsKey(str)){
                talk.put(str,1);
            }else{
                talk.put(str, talk.get(str)+1);
            }
        }
        return talk;
    }
}
